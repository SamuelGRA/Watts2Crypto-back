package com.watts2crypto.watts2crypto_backend.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.watts2crypto.watts2crypto_backend.models.Asic;
import com.watts2crypto.watts2crypto_backend.models.RendimientoAlgoritmo;
import com.watts2crypto.watts2crypto_backend.repositories.AsicRepository;

@Service
public class AsicService {

    private final AsicRepository repository;
    private final RestTemplate restTemplate;
    private final String key;

    public AsicService(AsicRepository repository, RestTemplate restTemplate,
            @Value("${whattomine.api.key}") String key) {
        this.repository = repository;
        this.restTemplate = restTemplate;
        this.key = key;
    }

    public void refreshAsics() {
        List<Asic> listaAsics = cargarAsicsDeWhatToMine();
        repository.deleteAll();
        repository.saveAll(listaAsics);
    }

    private List<Asic> cargarAsicsDeWhatToMine() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("User-Agent", "Mozilla/5.0");
        headers.set("Authorization", "Token " + key);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://whattomine.com/api/v1/asics",
                HttpMethod.GET,
                entity,
                String.class);

        String body = response.getBody();
        if (body == null || body.isBlank()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error en la llamada a WhatToMine");
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            List<Asic> res = new ArrayList<>();
            Map<String, LocalDate> nombreYFecha = new HashMap<>();
            ArrayNode root = (ArrayNode) mapper.readTree(body); // la API devuelve un array

            for (JsonNode asicNode : root) {
                String name = asicNode.get("name").asText();
                LocalDate releaseDate = LocalDate.parse(asicNode.get("release_date").asText());

                // Si hay un asic repetido en la respuesta de la API, cuya entrada es más
                // antigua que el asic que ya se tiene, no se actualiza
                // ese asic
                if (nombreYFecha.containsKey(name) && nombreYFecha.get(name).isBefore(releaseDate)) {
                    continue;
                }
                nombreYFecha.put(name, releaseDate);

                JsonNode algosNode = asicNode.get("algorithms");
                if (algosNode == null || !algosNode.isArray() || algosNode.isEmpty()) {
                    continue;
                }

                Map<String, RendimientoAlgoritmo> algoritmos = new HashMap<>();
                Double sumaConsumo = 0.0;
                Integer contadorConsumo = 0;
                Double sumaHashrate = 0.0;
                Integer contadorHashrate = 0;

                for (JsonNode algoNode : algosNode) {
                    String algoName = algoNode.get("name").asText();

                    Double hashrate = Double.parseDouble(algoNode.get("hashrate").asText());
                    Integer power = algoNode.get("power").asInt();

                    RendimientoAlgoritmo rendimiento = new RendimientoAlgoritmo();
                    rendimiento.setHashrate(hashrate);
                    rendimiento.setConsumo(power);

                    algoritmos.put(algoName, rendimiento);

                    sumaConsumo += power;
                    contadorConsumo++;
                    sumaHashrate += hashrate;
                    contadorHashrate++;
                }

                Integer consumoNominal = (int) (Math.round(sumaConsumo / contadorConsumo));
                Double hashrateNominal = sumaHashrate / contadorHashrate;
                Asic asic = new Asic(name, consumoNominal, hashrateNominal, algoritmos);
                res.add(asic);
            }
            return res;
        } catch (IOException e) {
            throw new IllegalStateException("Error parseando ASICs de WhatToMine", e);
        }

    }

    public List<Asic> findAllAsics() {
        try {
            return repository.findAll();
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public Asic findAsicByName(String nombre) {
        try {
            Optional<Asic> res = repository.findByNameIgnoreCase(nombre);
            if (!res.isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No se encontró ningún ASIC con ese nombre.");
            }
            return res.get();
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public List<String> findAllAsicNames() {
        try {
            Optional<List<String>> res = repository.findAllNames();
            if (!res.isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No se pudieron obtener los nombres de los ASICs.");
            }
            return res.get();
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public RendimientoAlgoritmo findHashrateYConsumoPorNombreYAlgoritmo(String nombre, String algoritmo) {
        try {
            Optional<RendimientoAlgoritmo> res = repository.findHashrateAndPowerByAsicAndAlgorithm(nombre, algoritmo);
            if (!res.isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No se encontraron resultados con esos parámetros.");
            }
            return res.get();
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public List<String> findNamesByAlgorithm(String algoritmo) {
        try {
            if (algoritmo == null || algoritmo.isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "El algoritmo no puede estar vacío.");
            }
            
            List<String> compatible = repository.findNamesByAlgorithm(algoritmo.trim());

            if (compatible.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No se encontraron ASICs compatibles con el algoritmo: " + algoritmo);
            }
            return compatible;
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
