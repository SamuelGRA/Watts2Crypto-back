package com.watts2crypto.watts2crypto_backend.models;

import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "gpu")
public class Gpu extends Hardware {

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "gpu_algorithms", joinColumns = @JoinColumn(name = "gpu_id"))
    @MapKeyColumn(name = "algoritmo")
    private Map<String, RendimientoAlgoritmo> algoritmos = new HashMap<>();
    //Map con nombre de algoritmo y RendimientoAlgoritmo, que contiene hashrate y consumo por cada
    //algoritmo

    public Gpu() {
    }

    public Gpu(String nombre, Integer consumoNominal,
               Map<String, RendimientoAlgoritmo> algorithms) {
        super(nombre, consumoNominal);
        this.algoritmos = algorithms;
    }

    public Map<String, RendimientoAlgoritmo> getAlgorithms() {
        return algoritmos;
    }

    public void setAlgorithms(Map<String, RendimientoAlgoritmo> algoritmos) {
        this.algoritmos = algoritmos;
    }

    public Double getSpeedHashesPorSegundo(String algoritmo) {
        if (algoritmos == null || algoritmo == null || !algoritmos.containsKey(algoritmo)) {
            return null;
        }
        return algoritmos.get(algoritmo).getHashrate();
    }

    public Double getConsumoEnWatts(String algoritmo) {
        if (algoritmos == null || algoritmo == null || !algoritmos.containsKey(algoritmo)) {
            return null;
        }
        return algoritmos.get(algoritmo).getConsumo();
    }
}
