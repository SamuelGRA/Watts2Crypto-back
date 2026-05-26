package com.watts2crypto.watts2crypto_backend.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.watts2crypto.watts2crypto_backend.models.MonedaTradicional;
import com.watts2crypto.watts2crypto_backend.models.SimbolosISOMonedas;
import com.watts2crypto.watts2crypto_backend.repositories.MonedaTradicionalRepository;
import com.watts2crypto.watts2crypto_backend.repositories.SimbolosISOMonedasRepository;

@Service
public class MonedaTradicionalService {

    private final List<String> monedasEnBD = List.of("USD", "GBP", "JPY", "CNY", "RUB", "AUD", "CAD", "CHF", "HKD",
            "BRL");
    private final MonedaTradicionalRepository repository;
    private final SimbolosISOMonedasRepository simbolosRepository;
    private final RestTemplate restTemplate;

    private static final String BASE_INTERNA = "EUR";

    public MonedaTradicionalService(MonedaTradicionalRepository repository, RestTemplate restTemplate, SimbolosISOMonedasRepository simbolosRepository) {
        this.repository = repository;
        this.restTemplate = restTemplate;
        this.simbolosRepository = simbolosRepository;
    }

    public void refreshMonedasTradicionales() {
        List<MonedaTradicional> listaMonedas = cargarMonedasDeFrankfurter();
        repository.deleteAll();
        repository.saveAll(listaMonedas);
    }

    public void refreshSymbols() {
        List<String> symbolsFromApi = cargarSymbolsDesdeFrankfurter();
        simbolosRepository.deleteAll();
        List<SimbolosISOMonedas> entities = symbolsFromApi.stream()
                .map(SimbolosISOMonedas::new)
                .toList();
        simbolosRepository.saveAll(entities);
    }

    private List<MonedaTradicional> cargarMonedasDeFrankfurter() {
        String simbolos = String.join(",", monedasEnBD);
        LocalDate actualidad = LocalDate.now(ZoneId.of("Europe/Madrid"));
        LocalDate limitePasado = actualidad.minusYears(1);
        String url = String.format("https://api.frankfurter.dev/v2/rates?from=%s&to=%s&base=%s&quotes=%s",
                limitePasado, actualidad, BASE_INTERNA, simbolos);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                String.class);

        String body = response.getBody();
        if (body == null || body.isBlank()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error en la llamada a Frankfurter");
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            List<MonedaTradicional> res = new ArrayList<>();
            JsonNode root = mapper.readTree(body);

            // La respuesta es un array de objetos
            if (!root.isArray()) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "La respuesta de Frankfurter no es un array válido.");
            }

            root.forEach(item -> {
                String monedaObjetivo = item.get("quote").asText();
                Double tasaCambio = item.get("rate").asDouble();
                LocalDate fecha = LocalDate.parse(item.get("date").asText());

                MonedaTradicional moneda = new MonedaTradicional();
                moneda.setMonedaBase(BASE_INTERNA);
                moneda.setMonedaObjetivo(monedaObjetivo);
                moneda.setTasaCambio(tasaCambio);
                moneda.setFecha(fecha);

                res.add(moneda);
            });

            return res;
        } catch (IOException e) {
            throw new IllegalStateException("Error parseando monedas de Frankfurter", e);
        }
    }

    // Metodo que hace una llamada directa a la API si el usuario busca una moneda
    // que no se guarda en la BD
    public MonedaTradicional findTasaCambioDirecta(String monedaBase, String monedaObjetivo, LocalDate fecha) {
        try {
            String url = String.format(
                    "https://api.frankfurter.dev/v2/rates?base=%s&quotes=%s",
                    monedaBase, monedaObjetivo);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    String.class);

            String body = response.getBody();
            if (body == null || body.isBlank()) {
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Error en la llamada a Frankfurter");
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(body);

            // Si la respuesta es un array, tomar el último elemento (más reciente)
            if (root.size() == 0) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontraron resultados con esos parámetros.");
            }
            JsonNode item = root.get(0);

            String baseRespuesta = item.get("base").asText();
            String quoteRespuesta = item.get("quote").asText();
            Double tasaCambio = item.get("rate").asDouble();
            LocalDate fechaRespuesta = LocalDate.parse(item.get("date").asText());

            MonedaTradicional moneda = new MonedaTradicional();
            moneda.setMonedaBase(baseRespuesta);
            moneda.setMonedaObjetivo(quoteRespuesta);
            moneda.setTasaCambio(tasaCambio);
            moneda.setFecha(fechaRespuesta);

            return moneda;

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // Metodo que obtiene historico directamente de la API para monedas no
    // persistidas en BD, equivalente al metodo anterior, pero para históricos
    public List<MonedaTradicional> findHistoricoTasaCambioDirecta(String monedaBase, String monedaObjetivo,
            LocalDate fechaInicio, LocalDate fechaFin) {
        try {
            LocalDate inicio = fechaInicio;
            LocalDate fin = fechaFin;
            if (inicio.isAfter(fin)) {
                LocalDate temp = inicio;
                inicio = fin;
                fin = temp;
            }

            String url = String.format(
                    "https://api.frankfurter.dev/v2/rates?from=%s&to=%s&base=%s&quotes=%s",
                    inicio, fin, monedaBase, monedaObjetivo);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    String.class);

            String body = response.getBody();
            if (body == null || body.isBlank()) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Error en la llamada historica a Frankfurter");
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(body);

            if (!root.isArray() || root.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No se encontraron resultados con esos parametros.");
            }

            List<MonedaTradicional> res = new ArrayList<>();
            root.forEach(item -> {
                MonedaTradicional moneda = new MonedaTradicional();
                moneda.setMonedaBase(item.get("base").asText());
                moneda.setMonedaObjetivo(item.get("quote").asText());
                moneda.setTasaCambio(item.get("rate").asDouble());
                moneda.setFecha(LocalDate.parse(item.get("date").asText()));
                res.add(moneda);
            });

            return completarSerieDiaria(res, monedaBase, monedaObjetivo, inicio, fin);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public Double convertirCantidad(Double cantidad, String monedaOrigen, String monedaDestino) {
        try {
            if (monedaOrigen.equals(monedaDestino)) {
                return cantidad;
            }

            // primero convierte la cantidad a euros (si no lo está ya)
            Double cantidadEnEur;
            if ("EUR".equals(monedaOrigen)) {
                cantidadEnEur = cantidad;
            } else {
                MonedaTradicional tasaOrigenAEur = findTasaCambioActualPorBaseYObjetivo(monedaOrigen, "EUR");
                cantidadEnEur = cantidad * tasaOrigenAEur.getTasaCambio();
            }

            // Paso 2: de EUR a destino
            if ("EUR".equals(monedaDestino)) {
                return cantidadEnEur;
            } else {
                MonedaTradicional tasaEurADestino = findTasaCambioActualPorBaseYObjetivo("EUR", monedaDestino);
                return cantidadEnEur * tasaEurADestino.getTasaCambio();
            }
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public List<MonedaTradicional> findAllMonedasTradicionales() {
        try {
            return repository.findAll();
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public MonedaTradicional findTasaCambioActualPorBaseYObjetivo(String monedaBase, String monedaObjetivo) {
        try {
            LocalDate fecha = LocalDate.now(ZoneId.of("Europe/Madrid"));
            Optional<MonedaTradicional> res = repository.findByExactDate( // Primero busca en base de datos
                    monedaBase, monedaObjetivo, fecha);

            if (res.isPresent()) {
                return res.get();
            }

            // Si la moneda a la que se quiere convertir no está en BD, hace llamada directa
            // a la API
            MonedaTradicional directa = findTasaCambioDirecta(monedaBase, monedaObjetivo, fecha);
            return directa;

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    public List<MonedaTradicional> findHistoricoPorBaseObjetivoYRangoFechas(
            String monedaBase,
            String monedaObjetivo,
            LocalDate fechaInicio,
            LocalDate fechaFin) {
        try {
            List<MonedaTradicional> res = repository.findByDateRange(
                    monedaBase, monedaObjetivo, fechaInicio, fechaFin);

            if (res.isEmpty()) {
                return findHistoricoTasaCambioDirecta(monedaBase, monedaObjetivo, fechaInicio, fechaFin);
            }
            return res;
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private List<MonedaTradicional> completarSerieDiaria(List<MonedaTradicional> puntosApi, String monedaBase,
            String monedaObjetivo, LocalDate inicio, LocalDate fin) {
        puntosApi.sort((a, b) -> a.getFecha().compareTo(b.getFecha()));

        List<MonedaTradicional> diaria = new ArrayList<>();
        MonedaTradicional ultimoDisponible = null;
        int indice = 0;

        for (LocalDate cursor = inicio; !cursor.isAfter(fin); cursor = cursor.plusDays(1)) {
            while (indice < puntosApi.size() && !puntosApi.get(indice).getFecha().isAfter(cursor)) {
                ultimoDisponible = puntosApi.get(indice);
                indice++;
            }

            MonedaTradicional fuente = ultimoDisponible != null ? ultimoDisponible : puntosApi.get(0);

            MonedaTradicional moneda = new MonedaTradicional();
            moneda.setMonedaBase(fuente.getMonedaBase() != null ? fuente.getMonedaBase() : monedaBase);
            moneda.setMonedaObjetivo(fuente.getMonedaObjetivo() != null ? fuente.getMonedaObjetivo() : monedaObjetivo);
            moneda.setTasaCambio(fuente.getTasaCambio());
            moneda.setFecha(cursor);
            diaria.add(moneda);
        }

        return diaria;
    }

    public List<String> findAllSymbols() {
        try {
            List<String> res = simbolosRepository.findAllSymbols();
            if (!res.isEmpty()) {
                return res;
            }

            List<String> symbolsFromApi = cargarSymbolsDesdeFrankfurter();
            List<SimbolosISOMonedas> entities = symbolsFromApi.stream()
                    .map(SimbolosISOMonedas::new)
                    .toList();
            simbolosRepository.saveAll(entities);

            return symbolsFromApi;
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private List<String> cargarSymbolsDesdeFrankfurter() {
        String url = "https://api.frankfurter.dev/v2/currencies";

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                String.class);

        String body = response.getBody();
        if (body == null || body.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error en la llamada a Frankfurter");
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(body);

            Set<String> symbols = new LinkedHashSet<>();
            if (root.isObject()) {
                root.fieldNames().forEachRemaining(symbols::add);
            } else if (root.isArray()) {
                for (JsonNode item : root) {
                    JsonNode iso = item.get("iso_code");
                    if (iso != null && !iso.asText().isBlank()) {
                        symbols.add(iso.asText());
                    }
                }
            }

            if (symbols.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No se encontraron simbolos en Frankfurter.");
            }

            return new ArrayList<>(symbols);
        } catch (IOException e) {
            throw new IllegalStateException("Error parseando simbolos de Frankfurter", e);
        }
    }
}
