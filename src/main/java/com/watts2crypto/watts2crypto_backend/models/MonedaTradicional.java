package com.watts2crypto.watts2crypto_backend.models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "monedas_tradicionales", indexes = {
    @Index(name = "idx_base_target_date", columnList = "monedaBase,monedaObjetivo,fecha")
})
public class MonedaTradicional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String monedaBase; // por simbolo

    @NotNull
    @Column(nullable = false)
    private String monedaObjetivo; // por simbolo

    @NotNull
    @Positive
    @Column(nullable = false)
    private Double tasaCambio; // 1 EUR = X USD

    @NotNull
    @Column(nullable = false)
    private LocalDate fecha; // "2024-01-15"

    public MonedaTradicional() {
    }

    public MonedaTradicional(String monedaBase, String monedaObjetivo, Double tasaCambio, LocalDate fecha) {
        this.monedaBase = monedaBase;
        this.monedaObjetivo = monedaObjetivo;
        this.tasaCambio = tasaCambio;
        this.fecha = fecha;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMonedaBase() {
        return monedaBase;
    }

    public void setMonedaBase(String monedaBase) {
        this.monedaBase = monedaBase;
    }

    public String getMonedaObjetivo() {
        return monedaObjetivo;
    }

    public void setMonedaObjetivo(String monedaObjetivo) {
        this.monedaObjetivo = monedaObjetivo;
    }

    public Double getTasaCambio() {
        return tasaCambio;
    }

    public void setTasaCambio(Double tasaCambio) {
        this.tasaCambio = tasaCambio;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

}
