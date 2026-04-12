package com.watts2crypto.watts2crypto_backend.models;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@MappedSuperclass
public abstract class Hardware {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String nombre;

    @Positive
    @Column
    private Integer consumoNominal;
    //Consumo general, (media de consumo por algoritmo en GPU y ASIC, valor directo en CPU)

    @Positive
    @Column
    private Double hashrateNominal;
    //Hashrate general, (media por algoritmo en GPU/ASIC, valor directo en CPU)

    protected Hardware() {
    }

    protected Hardware(String nombre, Integer consumoNominal, Double hashrateNominal) {
        this.nombre = nombre;
        this.consumoNominal = consumoNominal;
        this.hashrateNominal = hashrateNominal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getConsumoNominal() {
        return consumoNominal;
    }

    public void setConsumoNominal(Integer consumoNominal) {
        this.consumoNominal = consumoNominal;
    }

    public Double getHashrateNominal() {
        return hashrateNominal;
    }

    public void setHashrateNominal(Double hashrateNominal) {
        this.hashrateNominal = hashrateNominal;
    }
}
