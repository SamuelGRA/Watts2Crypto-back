package com.watts2crypto.watts2crypto_backend.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.watts2crypto.watts2crypto_backend.models.MonedaTradicional;
import com.watts2crypto.watts2crypto_backend.service.MonedaTradicionalService;

@RestController
@RequestMapping("/api/fiat")
public class MonedaTradicionalController {

    private final MonedaTradicionalService service;

    public MonedaTradicionalController(MonedaTradicionalService service) {
        this.service = service;
    }

    @GetMapping
    public List<MonedaTradicional> getAllMonedasTradicionales() {
        return service.findAllMonedasTradicionales();
    }

    @GetMapping("/simbolos")
    public List<String> getAllSymbols() {
        return service.findAllSymbols();
    }

    @GetMapping("/tasaCambio/{monedaBase}/{monedaObjetivo}")
    public MonedaTradicional getTasaPorBaseYObjetivo(@PathVariable String monedaBase,
            @PathVariable String monedaObjetivo) {
        return service.findTasaCambioActualPorBaseYObjetivo(monedaBase, monedaObjetivo);
    }

    @GetMapping("/historico/{monedaBase}/{monedaObjetivo}")
    public List<MonedaTradicional> getHistoricoPorBaseObjetivoYRangoFechas(@PathVariable String monedaBase,
            @PathVariable String monedaObjetivo,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {

        return service.findHistoricoPorBaseObjetivoYRangoFechas(monedaBase, monedaObjetivo, start, end);
    }

    @GetMapping("/convertirCantidad/{cantidad}")
    public Double convertirCantidad(@PathVariable Double cantidad, @RequestParam String monedaOrigen,
        @RequestParam String monedaObjetivo
    ) {
        return service.convertirCantidad(cantidad, monedaOrigen, monedaObjetivo);
    }

}
