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
    @Index(name = "idx_zone_fecha", columnList = "zone, fecha") //Se van a hacer muchas consultas sobre rangos de fecha y zonas
})
public class Electricidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String zone; // "ES", "FR", etc.

    @NotNull
    @Column(nullable = false)
    private LocalDateTime fecha;

    @NotNull
    @Positive
    @Column(nullable = false)
    private Double precioMwh;
    
}
