package com.watts2crypto.watts2crypto_backend.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.watts2crypto.watts2crypto_backend.models.Electricidad;
import com.watts2crypto.watts2crypto_backend.service.ElectricidadService;

@RestController
@RequestMapping("/api/electricidad")
public class ElectricidadController {

    private final ElectricidadService service;

    public ElectricidadController(ElectricidadService service) {
        this.service = service;
    }

    @GetMapping
    public List<Electricidad> getAll() {
        return service.findAll();
    }

    @GetMapping("/{zona}/historico")
    public List<Electricidad> getByDateRangeAndZone(@PathVariable String zona,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return service.findByDateRangeAndZone(zona, start, end);
    }

    @GetMapping("/{zona}/busquedaDirecta") // Ojo, llama directamente a la API, método de fallback
    public List<Electricidad> getByZoneDirect(@PathVariable String zona) {
        return service.findElectricidadDirectaPorZona(zona);
    }

    @GetMapping("/{zona}/{fecha}")
    public Double getPriceByExactDateAndZone(@PathVariable String zona,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fecha) {

        return service.findPriceByExactDateAndZone(zona, fecha);
    }

    @GetMapping("/{zona}/masReciente")
    public Electricidad getLatestPriceForZone(@PathVariable String zona) {
        return service.findLatestPriceInZone(zona);
    }

    @GetMapping("/zonas")
    public List<String> getAllZoneNames() {
        return service.findAllZones();
    }

    @GetMapping("/zonasDirecta")
    public List<String> findAllZonesForDirectSearch() {
        return service.findZonesForDirectSearch();
    }
}
