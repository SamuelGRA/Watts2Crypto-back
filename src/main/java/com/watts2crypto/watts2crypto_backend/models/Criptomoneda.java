package com.watts2crypto.watts2crypto_backend.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "criptomonedas")
public class Criptomoneda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String assetId; // "bitcoin", según coincap

    @NotNull
    @Column(nullable = false)
    private String symbol; // "BTC"

    @NotNull
    @Column(nullable = false)
    private String name; // "Bitcoin", como assetId, pero normalizado

    @NotNull
    @Column(nullable = false)
    private Double priceUsd;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime timestamp; //En milisegundos, habrá que hacer conversiones de fecha

    public Criptomoneda() {
    }

    public Criptomoneda(Long id, String assetId, String symbol, String name, Double priceUsd, LocalDateTime timestamp) {
        this.id = id;
        this.assetId = assetId;
        this.symbol = symbol;
        this.name = name;
        this.priceUsd = priceUsd;
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

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPriceUsd() {
        return priceUsd;
    }

    public void setPriceUsd(Double priceUsd) {
        this.priceUsd = priceUsd;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
}
