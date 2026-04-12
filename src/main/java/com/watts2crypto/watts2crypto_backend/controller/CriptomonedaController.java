package com.watts2crypto.watts2crypto_backend.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.watts2crypto.watts2crypto_backend.models.Criptomoneda;
import com.watts2crypto.watts2crypto_backend.models.CriptomonedaPrecio;
import com.watts2crypto.watts2crypto_backend.service.CriptomonedaService;

@RestController
@RequestMapping("/api/criptomonedas")
public class CriptomonedaController {

    private final CriptomonedaService service;

    public CriptomonedaController(CriptomonedaService service) {
        this.service = service;
    }

    @GetMapping()
    public List<Criptomoneda> getAll() {
        return service.findAllCriptomonedas();
    }

    @GetMapping("/nombres")
    public List<String> getAllNames() {
        return service.findAllNames();
    }

    @GetMapping("/{nombre}")
    public Criptomoneda getByName(@PathVariable String nombre) {
        return service.findCriptomonedaByName(nombre);
    }

    @GetMapping("/byAsset/{assetId}") // Ojo, llama a la API directamente, método de fallback cuando la moneda no está
                                      // en BD
    public Criptomoneda getByAssetId(@PathVariable String assetId) {
        return service.findCriptomonedaPorAssetId(assetId);
    }

    @GetMapping("/{nombre}/history")
    public List<CriptomonedaPrecio> getByDateRange(@PathVariable String nombre,
            @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return service.findByDateRange(nombre, start, end);
    }

}
