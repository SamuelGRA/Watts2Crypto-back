package com.watts2crypto.watts2crypto_backend.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "criptomonedas")
public class Criptomoneda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false, unique = true)
    private String assetId; // "bitcoin", identificador en coincap (basta con hacer lower sobre el nombre normal)

    @NotNull
    @Column(nullable = false, unique = true)
    private String simbolo; // "BTC"

    @NotNull
    @Column(nullable = false, unique = true)
    private String nombre; // "Bitcoin", como assetId, pero normalizado

    @NotNull
    @Column(nullable = false)
    private Double precioUsd;

    @NotBlank
    @Column(nullable = false)
    private String algoritmo;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime timestamp; //En milisegundos, habrá que hacer conversiones de fecha

    public Criptomoneda() {
    }

    public Criptomoneda(Long id, String assetId, String simbolo, String nombre, Double precioUsd, String algoritmo, LocalDateTime timestamp) {
        this.id = id;
        this.assetId = assetId;
        this.simbolo = simbolo;
        this.nombre = nombre;
        this.precioUsd = precioUsd;
        this.algoritmo = algoritmo;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPrecioUsd() {
        return precioUsd;
    }

    public void setPrecioUsd(Double precioUsd) {
        this.precioUsd = precioUsd;
    }

    public String getAlgoritmo() {
        return algoritmo;
    }

    public void setAlgoritmo(String algoritmo) {
        this.algoritmo = algoritmo;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
}
