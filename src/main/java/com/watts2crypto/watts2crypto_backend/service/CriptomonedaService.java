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

import jakarta.annotation.PostConstruct;

@Service
public class CriptomonedaService {

    private static final ZoneId ZONA_HORARIA = ZoneId.of("Europe/Madrid");
    private static final String COINCAP_BASE_URL = "https://rest.coincap.io/v3/assets/";
    private static final List<String> criptomonedasEnBD = List.of(
            "bitcoin",
            "ethereum",
            "tether",
            "usd-coin",
            "binance-coin",
            "xrp",
            "solana",
            "dogecoin",
            "cardano",
            "tron");

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

    @PostConstruct //Cambiar por scheduled, OJO: esta carga depende de las tasas de cambio, con lo cual se tiene que hace forzosamente
    //después de la carga de monedas tradicionales, para que los valores estén actualizados
    public void initCriptomonedas() {
        if (repository.count() > 0) {
            return;
        }

        List<Criptomoneda> criptomonedas = cargarCriptomonedasDeCoinCap();
        precioRepository.deleteAll();
        repository.deleteAll();
        repository.saveAll(criptomonedas);
    }

    private List<Criptomoneda> cargarCriptomonedasDeCoinCap() {
        List<Criptomoneda> res = new ArrayList<>();

        for (String assetId : criptomonedasEnBD) {
            try {
                Criptomoneda criptomoneda = construirCriptoConHistorico(assetId);
                if (criptomoneda != null) {
                    res.add(criptomoneda);
                }
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }

        return res;
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
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public List<CriptomonedaPrecio> findHistoricoDirectoPorAssetId(String assetId) {
        try {
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
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private Criptomoneda construirCriptoConHistorico(String assetId) throws IOException {
        JsonNode asset = llamarApiAsset(assetId).path("data");
        if (asset.isMissingNode() || asset.isNull()) {
            return null;
        }

        Criptomoneda criptomoneda = construirCriptomonedaDesdeAsset(asset);
        JsonNode historico = llamarApiHistorico(assetId).path("data");
        if (!historico.isArray() || historico.isEmpty()) {
            return criptomoneda;
        }

        for (JsonNode item : historico) {
            CriptomonedaPrecio precio = construirPrecioDesdeHistorico(item, criptomoneda);
            if (precio != null) {
                criptomoneda.addPrecio(precio);
            }
        }

        return criptomoneda;
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
            if(res.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron datos de criptomonedas");
            }
            return res;
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

    public List<CriptomonedaPrecio> findByDateRange(String nombre, LocalDate start, LocalDate end) {
        try {
            List<CriptomonedaPrecio> res = precioRepository.findByNombreAndDateRange(nombre, start, end);
            if(res.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron datos para ese rango de fechas");
            }
            return res;
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

}