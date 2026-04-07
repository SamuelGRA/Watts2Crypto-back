package com.watts2crypto.watts2crypto_backend.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.watts2crypto.watts2crypto_backend.models.MonedaTradicional;
import com.watts2crypto.watts2crypto_backend.repositories.MonedaTradicionalRepository;

import jakarta.annotation.PostConstruct;

@Service
public class MonedaTradicionalService {

    private final List<String> monedasEnBD = List.of("USD", "GBP", "JPY", "CNY", "RUB", "AUD", "CAD", "CHF", "HKD",
            "BRL");
    private final MonedaTradicionalRepository repository;
    private final RestTemplate restTemplate;

    private static final String BASE_INTERNA = "EUR";

    public MonedaTradicionalService(MonedaTradicionalRepository repository, RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    @PostConstruct // Sustituir por scheduled, todos los dias a las 18:00 (dos horas despues de que
                   // se actualice la API)
    public void initMonedasTradicionales() {
        if (repository.count() > 0) {
            return;
        }
        List<MonedaTradicional> listaMonedas = cargarMonedasDeFrankfurter();
        repository.deleteAll();
        repository.saveAll(listaMonedas);
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

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // Metodo que obtiene historico directamente de la API para monedas no
    // persistidas en BD, equivalente al metodo anterior, pero para históricos
    public List<MonedaTradicional> findHistoricoTasaCambioDirecta(String monedaBase, String monedaObjetivo) {
        try {
            
            LocalDate inicio = LocalDate.now(ZoneId.of("Europe/Madrid"));
            LocalDate fin = inicio.minusYears(1);

            String url = String.format(
                    "https://api.frankfurter.dev/v2/rates?from=%s&to=%s&base=%s&quotes=%s",
                    fin, inicio, monedaBase, monedaObjetivo);

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

            return res;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public Double convertirCantidad(Double cantidad, String monedaOrigen, String monedaDestino) {
        try {
            LocalDate fecha = LocalDate.now(ZoneId.of("Europe/Madrid"));
            if (monedaOrigen.equals(monedaDestino)) {
                return cantidad;
            }

            // primero convierte la cantidad a euros (si no lo está ya)
            Double cantidadEnEur;
            if ("EUR".equals(monedaOrigen)) {
                cantidadEnEur = cantidad;
            } else {
                MonedaTradicional tasaOrigenAEur = findTasaCambioPorBaseObjetivoYFecha(monedaOrigen, "EUR", fecha);
                cantidadEnEur = cantidad * tasaOrigenAEur.getTasaCambio();
            }

            // Paso 2: de EUR a destino
            if ("EUR".equals(monedaDestino)) {
                return cantidadEnEur;
            } else {
                MonedaTradicional tasaEurADestino = findTasaCambioPorBaseObjetivoYFecha("EUR", monedaDestino, fecha);
                return cantidadEnEur * tasaEurADestino.getTasaCambio();
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public List<MonedaTradicional> findAllMonedasTradicionales() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public MonedaTradicional findTasaCambioPorBaseObjetivoYFecha(String monedaBase, String monedaObjetivo,
            LocalDate fecha) {
        try {
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
                return findHistoricoTasaCambioDirecta(monedaBase, monedaObjetivo);
            }
            return res;
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
