package com.watts2crypto.watts2crypto_backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "metricas_minado")
public class MetricasMinado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Positive
    @Column(nullable = false, unique = true)
    private Integer whatToMineCoinId; //Identificador propio de la API (no tiene por qué coincidir con PK)

    @NotBlank
    @Column(nullable = false)
    private String nombreMoneda;

    @NotNull
    @Column(nullable = false)
    private Long nethash;

    @NotNull
    @Positive
    @Column(nullable = false)
    private Double blockTimeSegundos;

    @NotNull
    @Positive
    @Column(nullable = false)
    private Double blockReward;

    public MetricasMinado() {
    }

    public MetricasMinado(Integer whatToMineCoinId, String nombreMoneda, Long nethash,
            Double blockTimeSegundos, Double blockReward) {
        this.whatToMineCoinId = whatToMineCoinId;
        this.nombreMoneda = nombreMoneda;
        this.nethash = nethash;
        this.blockTimeSegundos = blockTimeSegundos;
        this.blockReward = blockReward;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getWhatToMineCoinId() {
        return whatToMineCoinId;
    }

    public void setWhatToMineCoinId(Integer whatToMineCoinId) {
        this.whatToMineCoinId = whatToMineCoinId;
    }

    public String getNombreMoneda() {
        return nombreMoneda;
    }

    public void setNombreMoneda(String nombreMoneda) {
        this.nombreMoneda = nombreMoneda;
    }

    public Long getNethash() {
        return nethash;
    }

    public void setNethash(Long nethash) {
        this.nethash = nethash;
    }

    public Double getBlockTimeSegundos() {
        return blockTimeSegundos;
    }

    public void setBlockTimeSegundos(Double blockTimeSegundos) {
        this.blockTimeSegundos = blockTimeSegundos;
    }

    public Double getBlockReward() {
        return blockReward;
    }

    public void setBlockReward(Double blockReward) {
        this.blockReward = blockReward;
    }
}
