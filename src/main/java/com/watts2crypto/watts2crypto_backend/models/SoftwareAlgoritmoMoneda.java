package com.watts2crypto.watts2crypto_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "software_algoritmo_moneda", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"software_id", "moneda", "algoritmo"})
})
public class SoftwareAlgoritmoMoneda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "software_id", nullable = false)
    @JsonIgnore
    private Software software;

    @NotBlank
    @Column(nullable = false)
    private String moneda; // "BTC", "ETH", etc.

    @NotBlank
    @Column(nullable = false)
    private String algoritmo; // "SHA-256", "Ethash", etc.

    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("100.0")
    @Column(nullable = false)
    private Double comision;

    public SoftwareAlgoritmoMoneda() {
    }

    public SoftwareAlgoritmoMoneda(Software software, String moneda, String algoritmo, Double comision) {
        this.software = software;
        this.moneda = moneda;
        this.algoritmo = algoritmo;
        this.comision = comision;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Software getSoftware() {
        return software;
    }

    public void setSoftware(Software software) {
        this.software = software;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getAlgoritmo() {
        return algoritmo;
    }

    public void setAlgoritmo(String algoritmo) {
        this.algoritmo = algoritmo;
    }

    public Double getComision() {
        return comision;
    }

    public void setComision(Double comision) {
        this.comision = comision;
    }
}
