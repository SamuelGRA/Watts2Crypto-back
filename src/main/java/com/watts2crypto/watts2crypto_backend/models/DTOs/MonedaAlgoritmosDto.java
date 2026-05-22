package com.watts2crypto.watts2crypto_backend.models.DTOs;

import java.util.LinkedHashSet;
import java.util.Set;

public class MonedaAlgoritmosDto {

    private String nombre;
    private Set<String> algoritmos = new LinkedHashSet<>();

    public MonedaAlgoritmosDto() {
    }

    public MonedaAlgoritmosDto(String nombre, Set<String> algoritmos) {
        this.nombre = nombre;
        this.algoritmos = algoritmos != null ? new LinkedHashSet<>(algoritmos) : new LinkedHashSet<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<String> getAlgoritmos() {
        return algoritmos;
    }

    public void setAlgoritmos(Set<String> algoritmos) {
        this.algoritmos = algoritmos != null ? new LinkedHashSet<>(algoritmos) : new LinkedHashSet<>();
    }
}