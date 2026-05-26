package com.watts2crypto.watts2crypto_backend.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.watts2crypto.watts2crypto_backend.models.Criptomoneda;
import com.watts2crypto.watts2crypto_backend.models.DTOs.MonedaAlgoritmosDto;
import com.watts2crypto.watts2crypto_backend.models.MetricasMinado;
import com.watts2crypto.watts2crypto_backend.repositories.CriptomonedaRepository;
import com.watts2crypto.watts2crypto_backend.repositories.MetricasMinadoRepository;

@Service
@DependsOn("criptomonedaService") //MetricasMinado depende de datos de Criptomonedas
public class MetricasMinadoService {

    private static final String WHATTOMINE_COINS_URL = "https://whattomine.com/api/v1/coins";
    private static final String BITCOIN_ASSET_ID = "bitcoin";

    private final MetricasMinadoRepository repository;
    private final CriptomonedaRepository criptomonedaRepository;
    private final RestTemplate restTemplate;
    private final String key;
    
    
    public MetricasMinadoService(MetricasMinadoRepository repository,
            CriptomonedaRepository criptomonedaRepository, RestTemplate restTemplate,
            @Value("${whattomine.api.key}") String key) {
        this.repository = repository;
        this.criptomonedaRepository = criptomonedaRepository;
        this.restTemplate = restTemplate;
        this.key = key;
    }

    public void refreshMetricasMinado() {
        List<MetricasMinado> metricas = cargarMetricasMinadoDeWhatToMine();
        repository.deleteAll();
        repository.saveAll(metricas);
    }

    private List<MetricasMinado> cargarMetricasMinadoDeWhatToMine() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("User-Agent", "Mozilla/5.0");
        headers.set("Authorization", "Token " + key);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                WHATTOMINE_COINS_URL,
                HttpMethod.GET,
                entity,
                String.class);

        String body = response.getBody();
        if (body == null || body.isBlank()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error en la llamada a WhatToMine para metricas de minado.");
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            ArrayNode root = (ArrayNode) mapper.readTree(body);
            List<MetricasMinado> res = new ArrayList<>();
            Double precioBitcoinEur = obtenerPrecioBitcoinEur();

            for (JsonNode item : root) {
                Integer coinId = item.path("id").isNumber() ? item.path("id").asInt() : null;
                String nombreMoneda = item.path("name").asText(null);
                String algoritmo = item.path("algorithm").asText(null);
                long nethash = item.path("nethash").asLong(0L);
                Double blockTime = Double.valueOf(item.path("block_time").asText(null));
                Double blockReward = item.path("block_reward24").asDouble(item.path("block_reward").asDouble(0.0));
                Double precioExchangeBtc = obtenerPrecioExchangeEnBtc(item.path("exchanges"));
                Double precioActualEur = obtenerPrecioActualEurPorMoneda(nombreMoneda, precioExchangeBtc, precioBitcoinEur);

                if (coinId == null || coinId <= 0) {
                    continue;
                }
                if (nombreMoneda == null || nombreMoneda.isBlank()) {
                    continue;
                }
                if (algoritmo == null || algoritmo.isBlank()) {
                    continue;
                }
                if (nethash <= 0L || blockTime <= 0.0 || blockReward <= 0.0) {
                    continue;
                }
                if (precioActualEur == null || precioActualEur <= 0.0) {
                    continue;
                }

                MetricasMinado metrica = new MetricasMinado(
                        coinId,
                        nombreMoneda,
                        algoritmo,
                        nethash,
                        blockTime,
                        blockReward,
                        precioActualEur);
                res.add(metrica);
            }

            return res;
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error parseando metricas de minado de WhatToMine: " + e.getMessage());
        }
    }

    private Double obtenerPrecioActualEurPorMoneda(String nombreMoneda, Double precioExchangeBtc, Double precioBitcoinEur) {
        try {
            String assetId = null;

            if (nombreMoneda != null && !nombreMoneda.isBlank()) {
                Criptomoneda criptoPorNombre = criptomonedaRepository.findByNameIgnoreCase(nombreMoneda).orElse(null);
                if (criptoPorNombre != null) {
                    assetId = criptoPorNombre.getAssetId();
                }
            }

            if (assetId != null && !assetId.isBlank()) {
                BigDecimal precioActual = criptomonedaRepository.findLatestPriceByAssetId(assetId).orElse(null);
                if (precioActual != null && precioActual.doubleValue() > 0.0) {
                    return precioActual.doubleValue();
                }
            }

            if (precioExchangeBtc != null && precioExchangeBtc > 0.0
                    && precioBitcoinEur != null && precioBitcoinEur > 0.0) {
                return precioExchangeBtc * precioBitcoinEur;
            }

            return null;
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            return null;
        }
    }

    private Double obtenerPrecioBitcoinEur() {
        try {
            BigDecimal precioBitcoin = criptomonedaRepository.findLatestPriceByAssetId(BITCOIN_ASSET_ID).orElse(null);
            if (precioBitcoin == null || precioBitcoin.doubleValue() <= 0.0) {
                return null;
            }
            return precioBitcoin.doubleValue();
        } catch (Exception e) {
            return null;
        }
    }

    //WhatToMine solo ofrece el precio de las monedas a través del cambio a BTC, como el precio real de bitcoin
    //lo conocemos a través de coincap, es cuestión de multiplicar
    private Double obtenerPrecioExchangeEnBtc(JsonNode exchangesNode) {
        if (exchangesNode == null || !exchangesNode.isArray() || exchangesNode.isEmpty()) {
            return null;
        }

        JsonNode primerExchange = exchangesNode.get(0);
        if (primerExchange == null || primerExchange.isMissingNode()) {
            return null;
        }

        JsonNode priceNode = primerExchange.path("price");
        if (priceNode.isMissingNode() || priceNode.isNull()) {
            return null;
        }

        double price = priceNode.asDouble(0.0);
        return price > 0.0 ? price : null;
    }

    public List<MetricasMinado> findAllMetricasMinado() {
        try {
            List<MetricasMinado> res = repository.findAll();
            if (res.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No se encontraron métricas de minado en base de datos.");
            }
            return res;
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public List<String> findAllNombresMonedasParaCalculo() {
        try {
            List<String> res = repository.findAllNombresMonedasParaCalculo();
            if (res.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No se encontraron monedas con métricas disponibles.");
            }
            return res;
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    public List<MonedaAlgoritmosDto> findAllMonedasParaCalculoConAlgoritmo() {
        try {
            List<MonedaAlgoritmosDto> res = repository.findAll().stream()
                    .collect(Collectors.groupingBy(
                            MetricasMinado::getNombreMoneda,
                            Collectors.mapping(MetricasMinado::getAlgoritmo,
                                    Collectors.toCollection(LinkedHashSet::new))))
                    .entrySet()
                    .stream()
                    .map(entry -> new MonedaAlgoritmosDto(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList());
            if (res.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No se encontraron monedas con métricas disponibles.");
            }
            return res;
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public MetricasMinado findMetricasByNombre(String nombreMoneda) {
        try {
            if (nombreMoneda == null || nombreMoneda.isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debes indicar el nombre de la moneda.");
            }

            return repository.findByNombreMonedaIgnoreCase(nombreMoneda.trim())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "No se encontraron métricas de minado para la moneda indicada."));
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
