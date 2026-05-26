package com.watts2crypto.watts2crypto_backend.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
import com.watts2crypto.watts2crypto_backend.models.Criptomoneda;
import com.watts2crypto.watts2crypto_backend.models.CriptomonedaPrecio;
import com.watts2crypto.watts2crypto_backend.repositories.CriptomonedaPrecioRepository;
import com.watts2crypto.watts2crypto_backend.repositories.CriptomonedaRepository;

@Service
public class CriptomonedaService {

    private static final ZoneId ZONA_HORARIA = ZoneId.of("Europe/Madrid");
    private static final String COINCAP_BASE_URL = "https://rest.coincap.io/v3/assets/";
    private static final List<String> criptomonedasEnBD = List.of("bitcoin", "ethereum-classic",
            "tether", "usd-coin", "binance-coin", "xrp", "solana", "dogecoin", "cardano", "tron", "steth",
            "hyperliquid", "monero", "chainlink", "zcash", "litecoin");

    private final CriptomonedaRepository repository;
    private final CriptomonedaPrecioRepository precioRepository;
    private final MonedaTradicionalService monedaTradicionalService;
    private final RestTemplate restTemplate;
    private final String key;

    public CriptomonedaService(CriptomonedaRepository repository, CriptomonedaPrecioRepository precioRepository,
            MonedaTradicionalService monedaTradicionalService,
            RestTemplate restTemplate,
            @Value("${coincap.api.key:}") String key) {
        this.repository = repository;
        this.precioRepository = precioRepository;
        this.monedaTradicionalService = monedaTradicionalService;
        this.restTemplate = restTemplate;
        this.key = key;
    }

    public void refreshCriptomonedas() {

        // Carga metadatos de todas las monedas disponibles
        List<Criptomoneda> metadatos = cargarMetadatosDeCoinCap();
        repository.deleteAll();
        repository.saveAll(metadatos);

        // Carga histórico solamente de las monedas que se persisten
        precioRepository.deleteAll();
        for (String assetId : criptomonedasEnBD) {
            try {
                Optional<Criptomoneda> persisted = repository.findByAssetIdIgnoreCase(assetId);
                if (persisted.isPresent()) {
                    cargarHistoricoParaAsset(assetId, persisted.get());
                }
            } catch (Exception e) {
                continue;
            }
        }
    }

    private List<Criptomoneda> cargarMetadatosDeCoinCap() {
        List<Criptomoneda> res = new ArrayList<>();
        try {
            JsonNode root = llamarApiMetadatos();
            JsonNode data = root.path("data");

            if (!data.isArray() || data.isEmpty()) {
                return res;
            }

            for (JsonNode asset : data) {
                try {
                    Criptomoneda c = construirCriptomonedaDesdeAsset(asset);
                    res.add(c);
                } catch (Exception e) {
                    continue;
                }
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return res;
    }

    private void cargarHistoricoParaAsset(String assetId, Criptomoneda persisted) {
        try {
            JsonNode root = llamarApiHistorico(assetId);
            JsonNode data = root.path("data");
            if (!data.isArray() || data.isEmpty()) {
                return;
            }

            List<CriptomonedaPrecio> precios = new ArrayList<>();
            for (JsonNode item : data) {
                CriptomonedaPrecio precio = construirPrecioDesdeHistorico(item, persisted);
                if (precio != null) {
                    precios.add(precio);
                }
            }

            if (!precios.isEmpty()) {
                precioRepository.saveAll(precios);
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public Criptomoneda findCriptomonedaDirectaPorAssetId(String assetId) {
        try {
            JsonNode root = llamarApiAsset(assetId);
            JsonNode data = root.path("data");
            if (data.isMissingNode() || data.isNull()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No se encontraron datos para la criptomoneda indicada.");
            }

            Criptomoneda criptomoneda = construirCriptomonedaDesdeAsset(data);
            CriptomonedaPrecio precioActual = construirPrecioActualDesdeAsset(
                    root.path("timestamp").asLong(Instant.now().toEpochMilli()),
                    data.path("priceUsd").asText(null),
                    criptomoneda);
            criptomoneda.addPrecio(precioActual);

            return criptomoneda;
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public List<CriptomonedaPrecio> findHistoricoDirectoPorSimbolo(String simbolo) {
        try {
            String assetId = repository.findAssetIdBySymbol(simbolo);
            JsonNode asset = llamarApiAsset(assetId).path("data");
            if (asset.isMissingNode() || asset.isNull()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No se encontraron datos para la criptomoneda indicada.");
            }

            Criptomoneda criptomoneda = construirCriptomonedaDesdeAsset(asset);
            JsonNode root = llamarApiHistorico(assetId);
            JsonNode data = root.path("data");
            if (!data.isArray() || data.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No se encontraron datos historicos para la criptomoneda indicada.");
            }

            List<CriptomonedaPrecio> res = new ArrayList<>();
            for (JsonNode item : data) {
                CriptomonedaPrecio precio = construirPrecioDesdeHistorico(item, criptomoneda);
                if (precio != null) {
                    res.add(precio);
                }
            }

            return res.stream()
                    .sorted(Comparator.comparing(CriptomonedaPrecio::getFecha))
                    .toList();
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private JsonNode llamarApiAsset(String assetId) throws IOException {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("User-Agent", "Mozilla/5.0");
        headers.set("Authorization", "Bearer " + key);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                COINCAP_BASE_URL + assetId.toLowerCase(),
                HttpMethod.GET,
                entity,
                String.class);

        String body = response.getBody();
        if (body == null || body.isBlank()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error en la llamada a CoinCap");
        }

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(body);
    }

    private JsonNode llamarApiMetadatos() throws IOException {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("User-Agent", "Mozilla/5.0");
        headers.set("Authorization", "Bearer " + key);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                COINCAP_BASE_URL,
                HttpMethod.GET,
                entity,
                String.class);

        String body = response.getBody();
        if (body == null || body.isBlank()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error en la llamada a CoinCap");
        }

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(body);
    }

    private JsonNode llamarApiHistorico(String assetId) throws IOException {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("User-Agent", "Mozilla/5.0");
        headers.set("Authorization", "Bearer " + key);

        LocalDate start = LocalDate.now(ZONA_HORARIA);
        LocalDate end = start.minusYears(1);
        long startMillis = start.atStartOfDay(ZONA_HORARIA).toInstant().toEpochMilli();
        long endMillis = end.plusDays(1).atStartOfDay(ZONA_HORARIA).minusNanos(1).toInstant().toEpochMilli();

        String url = COINCAP_BASE_URL + assetId.toLowerCase()
                + "/history?interval=d1&start=" + endMillis
                + "&end=" + startMillis;

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class);

        String body = response.getBody();

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(body);
    }

    private Criptomoneda construirCriptomonedaDesdeAsset(JsonNode assetNode) {
        Criptomoneda criptomoneda = new Criptomoneda();
        criptomoneda.setAssetId(assetNode.path("id").asText());
        criptomoneda.setSimbolo(assetNode.path("symbol").asText());
        criptomoneda.setNombre(assetNode.path("name").asText());
        return criptomoneda;
    }

    private CriptomonedaPrecio construirPrecioActualDesdeAsset(long timestampMillis, String precioRaw,
            Criptomoneda criptomoneda) {
        BigDecimal precioEur = convertirYRedondearPrecioEur(precioRaw);
        if (precioEur == null) {
            return null;
        }
        LocalDate fecha = Instant.ofEpochMilli(timestampMillis).atZone(ZONA_HORARIA).toLocalDate();
        return new CriptomonedaPrecio(precioEur, fecha, criptomoneda);
    }

    private CriptomonedaPrecio construirPrecioDesdeHistorico(JsonNode historyNode, Criptomoneda criptomoneda) {
        String precioRaw = historyNode.path("priceUsd").asText(null);
        BigDecimal precioEur = convertirYRedondearPrecioEur(precioRaw);
        if (precioEur == null) {
            return null;
        }

        String date = historyNode.path("date").asText(null);
        LocalDate fecha;
        if (date != null && !date.isBlank() && date.contains("T")) {
            fecha = LocalDate.parse(date.split("T")[0]);
        } else {
            long time = historyNode.path("time").asLong(0L);
            if (time <= 0L) {
                return null;
            }
            fecha = Instant.ofEpochMilli(time).atZone(ZONA_HORARIA).toLocalDate();
        }

        return new CriptomonedaPrecio(precioEur, fecha, criptomoneda);
    }

    private BigDecimal convertirYRedondearPrecioEur(String precioRaw) {
        if (precioRaw == null || precioRaw.isBlank()) {
            return null;
        }
        Double precioUsd = Double.valueOf(precioRaw);
        Double precioEur = monedaTradicionalService.convertirCantidad(precioUsd, "USD", "EUR");
        return BigDecimal.valueOf(precioEur).setScale(2, RoundingMode.HALF_UP);
    }

    public List<Criptomoneda> findAllCriptomonedas() {
        try {
            List<Criptomoneda> res = repository.findAll();
            if (res.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron datos de criptomonedas");
            }
            return res;
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public List<String> findAllNames() {
        try {
            List<String> res = repository.findAllNames();
            if (res.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No se pudieron obtener los nombres de las criptomonedas.");
            }
            return res;
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public List<String> findAllSymbols() {
        try {
            List<String> res = repository.findAllSymbols();
            if (res.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No se pudieron obtener los símbolos de las criptomonedas.");
            }
            return res;
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public Criptomoneda findCriptomonedaByName(String nombre) {
        try {
            Optional<Criptomoneda> res = repository.findByNameIgnoreCase(nombre);
            if (res.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Criptomoneda " + nombre + " no encontrada.");
            }
            return res.get();
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public Criptomoneda findCriptomonedaPorAssetId(String assetId) {
        try {
            Optional<Criptomoneda> res = repository.findByAssetIdIgnoreCase(assetId);
            if (res.isPresent()) {
                return res.get();
            }
            return findCriptomonedaDirectaPorAssetId(assetId);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public List<CriptomonedaPrecio> findByDateRange(String simbolo, LocalDate start, LocalDate end) {
        try {
            List<CriptomonedaPrecio> res = precioRepository.findByNombreAndDateRange(simbolo, start, end);
            if (start.isAfter(end)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "La fecha de inicio no puede ser posterior a la de fin de rango de busqueda");
            }
            if (res.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No se encontraron datos para ese rango de fechas");
            }
            return res;
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

}