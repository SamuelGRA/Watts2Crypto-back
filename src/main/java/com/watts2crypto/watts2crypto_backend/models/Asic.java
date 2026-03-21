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
@Table(name = "asic")
public class Asic extends Hardware {

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "asic_algorithms", joinColumns = @JoinColumn(name = "asic_id"))
    @MapKeyColumn(name = "algoritmo")
    private Map<String, RendimientoAlgoritmo> algoritmos = new HashMap<>();
    //Map con nombre de algoritmo y RendimientoAlgoritmo, que contiene hashrate y consumo por cada
    //algoritmo

    public Asic() {
    }

    public Asic(String nombre, Integer consumoNominal,
                Map<String, RendimientoAlgoritmo> algoritmos, String algoritmo) {
        super(nombre, consumoNominal);
        this.algoritmos = algoritmos;
    }

    public Map<String, RendimientoAlgoritmo> getAlgorithms() {
        return algoritmos;
    }

    public void setAlgorithms(Map<String, RendimientoAlgoritmo> algorithms) {
        this.algoritmos = algorithms;
    }

    public Double getSpeedHashesPorSegundo(String algoritmo) {
        if (algoritmos == null || algoritmo == null || !algoritmos.containsKey(algoritmo)) {
            return null;
        }
        return algoritmos.get(algoritmo).gethashrate();
    }

    public Double getSpeedGigahashesPorSegundo(String algoritmo) { //ASIC suele ser del orden de GH/s
        Double hashrate = getSpeedHashesPorSegundo(algoritmo);
        return hashrate == null ? null : hashrate / 1_000_000_000d;
    }

    public Double getConsumoEnWatts(String algoritmo) {
        if (algoritmos == null || algoritmo == null || !algoritmos.containsKey(algoritmo)) {
            return null;
        }
        return algoritmos.get(algoritmo).getConsumo();
    }
}
