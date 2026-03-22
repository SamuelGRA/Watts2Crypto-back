package com.watts2crypto.watts2crypto_backend.models;

import java.time.LocalDateTime;

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
@Table(name = "precios_electricidad", indexes = {
    @Index(name = "idx_zona_fecha", columnList = "zona, fecha") //Se van a hacer muchas consultas sobre rangos de fecha y zonas
})
public class Electricidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false, unique = true)
    private String zona; // "ES", "FR", etc.

    @NotNull
    @Column(nullable = false)
    private LocalDateTime fecha;

    @NotNull
    @Positive
    @Column(nullable = false)
    private Double precioMwh;

    public Electricidad() {
    }

    public Electricidad(String zona, LocalDateTime fecha, Double precioMwh) {
        this.zona = zona;
        this.fecha = fecha;
        this.precioMwh = precioMwh;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Double getPrecioMwh() {
        return precioMwh;
    }

    public void setPrecioMwh(Double precioMwh) {
        this.precioMwh = precioMwh;
    }
    
}
