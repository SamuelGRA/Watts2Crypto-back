package com.watts2crypto.watts2crypto_backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "cpu")
public class Cpu extends Hardware {

    @NotNull
    @Positive
    @Column(nullable = false)
    private Integer hashrate;

    public Cpu() {
    }

    public Cpu(String nombre, Integer consumoNominal, Integer hashrate) {
        super(nombre, consumoNominal, Double.valueOf(hashrate));
        this.hashrate = hashrate;
    }

    public Integer getHashrate() {
        return hashrate;
    }

    public void setHashrate(Integer hashrate) {
        this.hashrate = hashrate;
    }
}
