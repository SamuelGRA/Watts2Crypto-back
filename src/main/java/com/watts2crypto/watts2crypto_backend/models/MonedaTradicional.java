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
    @Index(name = "idx_base_target_date", columnList = "baseCurrency,targetCurrency,date")
})
public class MonedaTradicional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String baseCurrency; // por simbolo

    @NotNull
    @Column(nullable = false)
    private String targetCurrency; // por simbolo

    @NotNull
    @Positive
    @Column(nullable = false)
    private Double rate; // 1 EUR = X USD

    @NotNull
    @Column(nullable = false)
    private LocalDate date; // "2024-01-15"

    public MonedaTradicional() {
    }

    public MonedaTradicional(Long id, @NotNull String baseCurrency, @NotNull String targetCurrency,
            @NotNull @Positive Double rate, @NotNull LocalDate date) {
        this.id = id;
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
    
}
