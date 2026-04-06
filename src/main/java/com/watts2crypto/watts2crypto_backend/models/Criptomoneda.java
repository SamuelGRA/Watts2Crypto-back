package com.watts2crypto.watts2crypto_backend.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "criptomonedas")
public class Criptomoneda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false, unique = true)
    private String assetId; // "bitcoin", identificador en coincap

    @NotNull
    @Column(nullable = false, unique = true)
    private String simbolo; // "BTC"

    @NotNull
    @Column(nullable = false, unique = true)
    private String nombre; // "Bitcoin", como assetId, pero normalizado

    @OneToMany(mappedBy = "criptomoneda", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("fecha ASC")
    private List<CriptomonedaPrecio> historicoPrecios = new ArrayList<>();

    public Criptomoneda() {
    }

    public Criptomoneda(Long id, String assetId, String simbolo, String nombre) {
        this.id = id;
        this.assetId = assetId;
        this.simbolo = simbolo;
        this.nombre = nombre;
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

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<CriptomonedaPrecio> getHistoricoPrecios() {
        return historicoPrecios;
    }

    public void setHistoricoPrecios(List<CriptomonedaPrecio> historicoPrecios) {
        this.historicoPrecios.clear();
        for (CriptomonedaPrecio precio : historicoPrecios) {
            addPrecio(precio);
        }
    }

    public void addPrecio(CriptomonedaPrecio precio) {
        precio.setCriptomoneda(this);
        this.historicoPrecios.add(precio);
    }
    
}
