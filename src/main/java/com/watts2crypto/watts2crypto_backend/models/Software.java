//Esta entidad representará un software de minería
package com.watts2crypto.watts2crypto_backend.models;

import java.util.List;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
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

    public enum TipoSoftware {
        MINERO, //Ejecuta la minería directamente
        PLATAFORMA, //Abstrae la minería, más comprensible para usuarios
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

    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("100.0")
    @Column(nullable = false)
    private Double comision;
    
    @NotEmpty
    @Column(nullable = false)
    private List<String> algoritmos;

    @NotEmpty
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<SistemaOperativo> sistemas;

    @NotEmpty
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<TipoSoftware> tipoSoftware;

    public Software(){
    }

    public Software(String nombre, Set<HardwareUsable> hardwareUsable, Double comision, List<String> algoritmos,
            Set<SistemaOperativo> sistemas, Set<TipoSoftware> tipoSoftware) {
        this.nombre = nombre;
        this.hardwareUsable = hardwareUsable;
        this.comision = comision;
        this.algoritmos = algoritmos;
        this.sistemas = sistemas;
        this.tipoSoftware = tipoSoftware;
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

    public Double getComision() {
        return comision;
    }

    public void setComision(Double comision) {
        this.comision = comision;
    }

    public List<String> getAlgoritmos() {
        return algoritmos;
    }

    public void setAlgoritmos(List<String> algoritmos) {
        this.algoritmos = algoritmos;
    }

    public Set<SistemaOperativo> getSistemas() {
        return sistemas;
    }

    public void setSistemas(Set<SistemaOperativo> sistemas) {
        this.sistemas = sistemas;
    }

    public Set<TipoSoftware> getTipoSoftware() {
        return tipoSoftware;
    }

    public void setTipoSoftware(Set<TipoSoftware> tipoSoftware) {
        this.tipoSoftware = tipoSoftware;
    }
}