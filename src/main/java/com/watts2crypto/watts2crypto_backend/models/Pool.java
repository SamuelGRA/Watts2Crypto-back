//Esta entidad representará una pool de minería
package com.watts2crypto.watts2crypto_backend.models;

import java.util.List;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "pools")
public class Pool {

    public enum EsquemaDePago {
        PPS, //pay per share
        PPLNS, //pay per last N shares (pago por contribucion reciente)
        SOLO, //pago solo si se encuentre el bloque
        PPS_PLUS, //pps + tarifas de transaccion
        FPPS //pps + otras tarifas
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @Column(nullable = false)
    private String nombre;

    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("100.0")
    @Column(nullable = false)
    private Double comision;

    @NotNull
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<EsquemaDePago> esquemaDePago; // PPS, PPLNS, SOLO

    @ElementCollection
    @CollectionTable(name = "pool_regiones", joinColumns = @JoinColumn(name = "pool_id"))
    @Column(name = "region")
    @NotEmpty
    private List<String> regiones; // "EU", "US", "ASIA", regiones donde se ubican los servidores

    @ElementCollection
    @CollectionTable(name = "pool_monedas", joinColumns = @JoinColumn(name = "pool_id"))
    @Column(name = "moneda")
    @NotEmpty
    private List<String> monedas; // "BTC", "XMR", monedas con las que la pool opera

    @ElementCollection
    @CollectionTable(name = "pool_algoritmos", joinColumns = @JoinColumn(name = "pool_id"))
    @Column(name = "algoritmo")
    @NotEmpty
    private List<String> algoritmos; // "SHA-256", "RandomX", algoritmos soportados

    public Pool(){
    }

    public Pool(Long id, String nombre, Double comision, Set<EsquemaDePago> esquemaDePago,
        List<String> regiones, List<String> monedas, List<String> algoritmos) {
        this.id = id;
        this.nombre = nombre;
        this.comision = comision;
        this.esquemaDePago = esquemaDePago;
        this.regiones = regiones;
        this.monedas = monedas;
        this.algoritmos = algoritmos;
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

    public Double getComision() {
        return comision;
    }

    public void setComision(Double comision) {
        this.comision = comision;
    }

    public Set<EsquemaDePago> getEsquemaDePago() {
        return esquemaDePago;
    }

    public void setEsquemaDePago(Set<EsquemaDePago> esquemaDePago) {
        this.esquemaDePago = esquemaDePago;
    }

    public List<String> getRegiones() {
        return regiones;
    }

    public void setRegiones(List<String> regiones) {
        this.regiones = regiones;
    }

    public List<String> getMonedas() {
        return monedas;
    }

    public void setMonedas(List<String> monedas) {
        this.monedas = monedas;
    }

    public List<String> getAlgoritmos() {
        return algoritmos;
    }

    public void setAlgoritmos(List<String> algoritmos) {
        this.algoritmos = algoritmos;
    }

    
}