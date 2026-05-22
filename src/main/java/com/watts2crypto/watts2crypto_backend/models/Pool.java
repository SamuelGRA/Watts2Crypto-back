//Esta entidad representará una pool de minería
package com.watts2crypto.watts2crypto_backend.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "pool")
public class Pool {

    public enum EsquemaDePago {
        PPS, //pay per share
        PPLNS, //pay per last N shares (pago por contribucion reciente)
        SOLO, //pago solo si se encuentra el bloque
        PROP, //pay per round
        PPLNSBF, //PPLNS con bonus/fees específicos de la pool
        PPS_PLUS, //pps + tarifas de transaccion
        FPPS //pps + otras tarifas
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @Column(nullable = false, unique = true)
    private String nombre;

    @NotNull
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<EsquemaDePago> esquemaDePago; // PPS, PPLNS, SOLO, PROP, PPLNSBF, PPS_PLUS, FPPS

    @ElementCollection
    @CollectionTable(name = "pool_regiones", joinColumns = @JoinColumn(name = "pool_id"))
    @Column(name = "region")
    @NotEmpty
    private List<String> regiones; // "EU", "US", "ASIA", regiones en las que se ubican los servidores

    @OneToMany(mappedBy = "pool", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PoolMonedaComision> detallesMonedaComision = new ArrayList<>();

    private String hashrateSlug; // identificador en hashrate.no

    public Pool(){
    }

    public Pool(String nombre, Set<EsquemaDePago> esquemaDePago,
        List<String> regiones, String hashrateSlug) {
        this.nombre = nombre;
        this.esquemaDePago = esquemaDePago;
        this.regiones = regiones;
        this.hashrateSlug = hashrateSlug;
    }

    public String getHashrateSlug() {
        return hashrateSlug;
    }

    public void setHashrateSlug(String hashrateSlug) {
        this.hashrateSlug = hashrateSlug;
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

    public List<PoolMonedaComision> getDetallesMonedaComision() {
        return detallesMonedaComision;
    }

    public void setDetallesMonedaComision(List<PoolMonedaComision> detallesMonedaComision) {
        this.detallesMonedaComision = detallesMonedaComision;
    }

    public void addDetalleMonedaComision(PoolMonedaComision detalle) {
        this.detallesMonedaComision.add(detalle);
        detalle.setPool(this);
    }

    
}