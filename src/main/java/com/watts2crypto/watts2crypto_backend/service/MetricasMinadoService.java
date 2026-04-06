package com.watts2crypto.watts2crypto_backend.service;

import java.util.ArrayList;
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
import com.watts2crypto.watts2crypto_backend.repositories.MetricasMinadoRepository;

import jakarta.annotation.PostConstruct;

@Service
public class MetricasMinadoService {

    private static final String WHATTOMINE_COINS_URL = "https://whattomine.com/api/v1/coins";

    private final MetricasMinadoRepository repository;
    private final RestTemplate restTemplate;
    private final String key;

    public MetricasMinadoService(MetricasMinadoRepository repository, RestTemplate restTemplate,
            @Value("${whattomine.api.key}") String key) {
        this.repository = repository;
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

            for (JsonNode item : root) {
                Integer coinId = item.path("id").isNumber() ? item.path("id").asInt() : null;
                String nombreMoneda = item.path("name").asText(null);
                long nethash = item.path("nethash").asLong(0L);
                Double blockTime = Double.valueOf(item.path("block_time").asText(null));
                Double blockReward = item.path("block_reward24").asDouble(item.path("block_reward").asDouble(0.0));

                if (coinId == null || coinId <= 0) {
                    continue;
                }
                if (nombreMoneda == null || nombreMoneda.isBlank()) {
                    continue;
                }
                if (nethash <= 0L || blockTime <= 0.0 || blockReward <= 0.0) {
                    continue;
                }

                MetricasMinado metrica = new MetricasMinado(
                        coinId,
                        nombreMoneda,
                        nethash,
                        blockTime,
                        blockReward);
                res.add(metrica);
            }

            return res;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error parseando metricas de minado de WhatToMine: " + e.getMessage());
        }
    }
}
