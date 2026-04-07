package com.watts2crypto.watts2crypto_backend.models;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "criptomoneda_precios", indexes = {
        @Index(name = "idx_cripto_fecha", columnList = "criptomoneda_id,fecha")
}, uniqueConstraints = @UniqueConstraint(columnNames = { "criptomoneda_id", "fecha" }))
public class CriptomonedaPrecio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Positive
    @Column(nullable = false, precision = 20, scale = 2)
    private BigDecimal precioEur;

    @NotNull
    @Column(nullable = false)
    private LocalDate fecha;

    @NotNull
    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "criptomoneda_id", nullable = false)
    private Criptomoneda criptomoneda;

    public CriptomonedaPrecio() {
    }

    public CriptomonedaPrecio(BigDecimal precioEur, LocalDate fecha, Criptomoneda criptomoneda) {
        this.precioEur = precioEur;
        this.fecha = fecha;
        this.criptomoneda = criptomoneda;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrecioEur() {
        return precioEur;
    }

    public void setPrecioUsd(BigDecimal precioEur) {
        this.precioEur = precioEur;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Criptomoneda getCriptomoneda() {
        return criptomoneda;
    }

    public void setCriptomoneda(Criptomoneda criptomoneda) {
        this.criptomoneda = criptomoneda;
    }
}
