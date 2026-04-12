package com.watts2crypto.watts2crypto_backend.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
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
import com.watts2crypto.watts2crypto_backend.models.MetricasMinado;
import com.watts2crypto.watts2crypto_backend.repositories.CriptomonedaRepository;
import com.watts2crypto.watts2crypto_backend.repositories.MetricasMinadoRepository;

import jakarta.annotation.PostConstruct;

@Service
public class MetricasMinadoService {

    private static final String WHATTOMINE_COINS_URL = "https://whattomine.com/api/v1/coins";

    private final MetricasMinadoRepository repository;
    private final MonedaTradicionalService monedaTradicionalService;
    private final CriptomonedaRepository criptomonedaRepository;
    private final RestTemplate restTemplate;    
    private final String key;
    
    
    public MetricasMinadoService(MetricasMinadoRepository repository, MonedaTradicionalService monedaTradicionalService,
            CriptomonedaRepository criptomonedaRepository, RestTemplate restTemplate,
            @Value("${whattomine.api.key}") String key) {
        this.repository = repository;
        this.monedaTradicionalService = monedaTradicionalService;
        this.criptomonedaRepository = criptomonedaRepository;
        this.restTemplate = restTemplate;
        this.key = key;
    }

    @PostConstruct // Sustituir por scheduled
    public void initMetricasMinado() {
        if (repository.count() > 0) {
            return;
        }
        List<MetricasMinado> metricas = cargarMetricasMinadoDeWhatToMine();
        repository.deleteAll();
        repository.saveAll(metricas);
    }

    public List<MetricasMinado> findAllMetricasMinado() {
        try {
            List<MetricasMinado> res = repository.findAll();
            if (res.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No se encontraron metricas de minado en base de datos.");
            }
            return res;
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
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
                Double precioMedioEur = extraerPrecioMedioEur(item.path("exchanges"), nombreMoneda, precioBitcoinEur);

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
                if (precioMedioEur == null || precioMedioEur <= 0.0) {
                    continue;
                }

                MetricasMinado metrica = new MetricasMinado(
                        coinId,
                        nombreMoneda,
                        algoritmo,
                        nethash,
                        blockTime,
                        blockReward,
                        precioMedioEur);
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

    private Double extraerPrecioMedioEur(JsonNode exchangesNode, String nombreMoneda, Double precioBitcoinEur) {
        if (exchangesNode == null || !exchangesNode.isArray() || exchangesNode.isEmpty()) {
            return null;
        }

        List<Double> precios = new ArrayList<>();

        for (JsonNode exchange : exchangesNode) {
            JsonNode precioNode = exchange.path("price");
            double precio = precioNode.asDouble();
            if (precio > 0.0 && Double.isFinite(precio)) {
                precios.add(precio);
            }
        }

        if (precios.isEmpty()) {
            return null;
        }

        List<Double> preciosFiltrados = filtrarOutliersIqr(precios);
        double precioRepresentativo = calcularMediana(preciosFiltrados);

        // Para Bitcoin el precio suele venir ya en dólares, así que se convierte a euros.
        if (nombreMoneda != null && nombreMoneda.equalsIgnoreCase("Bitcoin")) {
            return convertirUsdAEurRedondeado(precioRepresentativo);
        }

        // Para el resto se aproxima respecto a BTC y se normaliza a euros.
        if (precioBitcoinEur == null || precioBitcoinEur <= 0.0) {
            return null;
        }

        return redondear(precioRepresentativo * precioBitcoinEur, 8);
    }

    private Double obtenerPrecioBitcoinEur() {
        try {
            BigDecimal precioActualBitcoin = criptomonedaRepository.findLatestPriceByAssetId("bitcoin")
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "No se encontró el último precio de bitcoin."));
            return precioActualBitcoin.doubleValue();
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error parseando el precio de Bitcoin desde CoinCap: " + e.getMessage());
        }
    }

    private Double convertirUsdAEurRedondeado(Double precioUsd) {
        if (precioUsd == null || precioUsd <= 0.0) {
            return null;
        }

        Double precioEur = monedaTradicionalService.convertirCantidad(precioUsd, "USD", "EUR");
        return redondear(precioEur, 8);
    }

    //Elimina valores que despunten mucho (podrían provocar desviaciones en los precios)
    private List<Double> filtrarOutliersIqr(List<Double> valores) {
        if (valores.size() < 4) {
            return valores;
        }

        List<Double> ordenados = new ArrayList<>(valores);
        Collections.sort(ordenados);

        double q1 = calcularPercentil(ordenados, 25.0);
        double q3 = calcularPercentil(ordenados, 75.0);
        double iqr = q3 - q1;

        if (iqr <= 0.0) {
            return ordenados;
        }

        double min = q1 - (1.5 * iqr);
        double max = q3 + (1.5 * iqr);

        List<Double> filtrados = new ArrayList<>();
        for (Double valor : ordenados) {
            if (valor >= min && valor <= max) {
                filtrados.add(valor);
            }
        }

        return filtrados.isEmpty() ? ordenados : filtrados;
    }

    //La mediana es mejor que la media en estos casos
    private double calcularMediana(List<Double> valores) {
        if (valores == null || valores.isEmpty()) {
            return 0.0;
        }

        List<Double> ordenados = new ArrayList<>(valores);
        Collections.sort(ordenados);

        int n = ordenados.size();
        if (n % 2 == 1) {
            return ordenados.get(n / 2);
        }

        return (ordenados.get((n / 2) - 1) + ordenados.get(n / 2)) / 2.0;
    }

    private double calcularPercentil(List<Double> valoresOrdenados, double percentil) {
        int n = valoresOrdenados.size();
        if (n == 1) {
            return valoresOrdenados.get(0);
        }

        double posicion = (percentil / 100.0) * (n - 1);
        int base = (int) Math.floor(posicion);
        int techo = (int) Math.ceil(posicion);

        if (base == techo) {
            return valoresOrdenados.get(base);
        }

        double fraccion = posicion - base;
        return valoresOrdenados.get(base) + fraccion * (valoresOrdenados.get(techo) - valoresOrdenados.get(base));
    }

    private Double redondear(Double valor, int escala) {
        if (valor == null || !Double.isFinite(valor)) {
            return null;
        }
        return BigDecimal.valueOf(valor).setScale(escala, RoundingMode.HALF_UP).doubleValue();
    }
}
