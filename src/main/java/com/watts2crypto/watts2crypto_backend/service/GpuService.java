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
import com.watts2crypto.watts2crypto_backend.models.Gpu;
import com.watts2crypto.watts2crypto_backend.models.RendimientoAlgoritmo;
import com.watts2crypto.watts2crypto_backend.repositories.GpuRepository;

import jakarta.annotation.PostConstruct;

@Service
public class GpuService {

    private final GpuRepository repository;
    private final RestTemplate restTemplate;
    private final String key;

    // Así se puede inyectar la key de las APIs (la forma mas sencilla que he
    // encontrado)
    public GpuService(GpuRepository repository, RestTemplate restTemplate, @Value("${whattomine.api.key}") String key) {
        this.repository = repository;
        this.restTemplate = restTemplate;
        this.key = key;
    }

    @PostConstruct // Sustituir por scheduled
    public void initGpus() {
        if (repository.count() > 0) {
            return;
        }
        List<Gpu> listaGpus = cargarGpusDeWhatToMine();
        repository.deleteAll(); // Borra todos los datos anteriores para mantenerse al día con lo que llega de
                                // la API y evitar problemas de unicidad
        repository.saveAll(listaGpus);
    }

    private List<Gpu> cargarGpusDeWhatToMine() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("User-Agent", "Mozilla/5.0");
        headers.set("Authorization", "Token " + key);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://whattomine.com/api/v1/gpus",
                HttpMethod.GET,
                entity,
                String.class);

        String body = response.getBody();
        if (body == null || body.isBlank()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error en la llamada a WhatToMine");
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            List<Gpu> res = new ArrayList<>();
            Map<String, LocalDate> nombreYFecha = new HashMap<>();
            ArrayNode root = (ArrayNode) mapper.readTree(body); // la API devuelve un array

            for (JsonNode gpuNode : root) {
                String name = gpuNode.get("name").asText();
                LocalDate releaseDate = LocalDate.parse(gpuNode.get("release_date").asText());

                // Si hay una gpu repetida en la respuesta de la API, cuya entrada es más
                // antigua que la gpu que ya se tiene, no se actualiza
                // esa gpu, esto se hace porque esta API puede dar entradas repetidas
                if (nombreYFecha.containsKey(name) && nombreYFecha.get(name).isBefore(releaseDate)) {
                    continue;
                }
                nombreYFecha.put(name, releaseDate);

                JsonNode algosNode = gpuNode.get("algorithms");
                if (algosNode == null || !algosNode.isArray() || algosNode.isEmpty()) {
                    continue;
                }

                Map<String, RendimientoAlgoritmo> algoritmos = new HashMap<>();
                Double sumaConsumo = 0.0;
                Integer contadorConsumo = 0;

                for (JsonNode algoNode : algosNode) {
                    String algoName = algoNode.get("name").asText();

                    Double hashrate = Double.parseDouble(algoNode.get("hashrate").asText());
                    Double power = algoNode.get("power").asDouble();

                    RendimientoAlgoritmo rendimiento = new RendimientoAlgoritmo();
                    rendimiento.setHashrate(hashrate);
                    rendimiento.setConsumo(power);

                    algoritmos.put(algoName, rendimiento);

                    sumaConsumo += power;
                    contadorConsumo++;
                }

                Integer consumoNominal = (int) (Math.round(sumaConsumo / contadorConsumo));
                Gpu gpu = new Gpu(name, consumoNominal, algoritmos);
                res.add(gpu);
            }

            return res;
        } catch (IOException e) {
            throw new IllegalStateException("Error parseando GPUs de WhatToMine", e);
        }

    }

    public List<Gpu> findAllGpus() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    public List<String> findAllGpuNames() {
        try {
            Optional<List<String>> res = repository.findAllNames();
            if (!res.isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No se pudieron obtener los nombres de las tarjetas gráficas.");
            }
            return res.get();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public RendimientoAlgoritmo findHashrateYConsumoPorNombreYAlgoritmo(String nombre, String algoritmo) {
        try {
            Optional<RendimientoAlgoritmo> res = repository.findHashrateAndPowerByGpuAndAlgorithm(nombre, algoritmo);
            if (!res.isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No se encontraron resultados con esos parámetros.");
            }
            return res.get();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
