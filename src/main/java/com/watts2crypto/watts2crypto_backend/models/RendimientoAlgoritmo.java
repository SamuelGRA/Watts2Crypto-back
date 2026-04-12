package com.watts2crypto.watts2crypto_backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Embeddable
public class RendimientoAlgoritmo {
    //Entidad que contiene velocidad (hash por segundo, hay metodo para normalizar los valores) 
    //y consumo por cada algoritmo de minado soportado por el hardware

    @NotNull
    @Positive
    @Column(nullable = false)
    private Double hashrate;

    @NotNull
    @Positive
    @Column(nullable = false)
    private Integer consumo;

    public RendimientoAlgoritmo() {
    }

    public RendimientoAlgoritmo(Double hashrate, Integer consumo) {
        this.hashrate = hashrate;
        this.consumo = consumo;
    }

    public Double getHashrate() {
        return hashrate;
    }

    public void setHashrate(Double hashrate) {
        this.hashrate = hashrate;
    }

    public Integer getConsumo() {
        return consumo;
    }

    public void setConsumo(Integer consumo) {
        this.consumo = consumo;
    }
    
}
