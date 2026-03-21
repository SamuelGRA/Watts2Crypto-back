package com.watts2crypto.watts2crypto_backend.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "cpu")
public class Cpu extends Hardware {

    @NotNull
    @Positive
    private Double hashrate;

    @NotNull
    @Positive
    private Double consumo; //TDP en TechPowerup

    public Cpu() {
    }

    public Cpu(String nombre, Integer consumoNominal, Double hashrate, Double consumo) {
        super(nombre, consumoNominal);
        this.hashrate = hashrate;
        this.consumo = consumo;
    }

    public Double getHashrate() {
        return hashrate;
    }

    public void setHashrate(Double hashrate) {
        this.hashrate = hashrate;
    }

    public Double getConsumo() {
        return consumo;
    }

    public void setConsumo(Double consumo) {
        this.consumo = consumo;
    }
    

}
