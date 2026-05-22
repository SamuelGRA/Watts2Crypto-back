//Esta entidad representará un software de minería
package com.watts2crypto.watts2crypto_backend.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "software")
public class Software {

    public enum HardwareUsable {
        GPU,
        CPU,
        ASIC
    }

    public enum SistemaOperativo {
        WINDOWS,
        LINUX,
        MACOS
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @Column(nullable = false, unique = true)
    private String nombre;

    @NotEmpty
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<HardwareUsable> hardwareUsable;

    private String hashrateSlug; // identificador en hashrate.no

    @OneToMany(mappedBy = "software", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SoftwareAlgoritmoMoneda> detallesAlgoritmoMoneda = new ArrayList<>();

    @NotEmpty
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<SistemaOperativo> sistemas;

    public Software(){
    }

    public Software(String nombre, Set<HardwareUsable> hardwareUsable,
            Set<SistemaOperativo> sistemas, String hashrateSlug) {
        this.nombre = nombre;
        this.hardwareUsable = hardwareUsable;
        this.sistemas = sistemas;
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

    public Long setId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<HardwareUsable> getHardwareUsable() {
        return hardwareUsable;
    }

    public void setHardwareUsable(Set<HardwareUsable> hardwareUsable) {
        this.hardwareUsable = hardwareUsable;
    }

    public List<SoftwareAlgoritmoMoneda> getDetallesAlgoritmoMoneda() {
        return detallesAlgoritmoMoneda;
    }

    public void setDetallesAlgoritmoMoneda(List<SoftwareAlgoritmoMoneda> detallesAlgoritmoMoneda) {
        this.detallesAlgoritmoMoneda = detallesAlgoritmoMoneda;
    }

    public void addDetalleAlgoritmoMoneda(SoftwareAlgoritmoMoneda detalle) {
        this.detallesAlgoritmoMoneda.add(detalle);
        detalle.setSoftware(this);
    }

    public Set<SistemaOperativo> getSistemas() {
        return sistemas;
    }

    public void setSistemas(Set<SistemaOperativo> sistemas) {
        this.sistemas = sistemas;
    }
}