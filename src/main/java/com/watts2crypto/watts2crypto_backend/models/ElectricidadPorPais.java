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
@Table(name = "electricidad_por_pais")
public class ElectricidadPorPais {
    //Esta entidad va a proporcionar los precios para los cálculos de rentabilidad, los datos de la entidad Electricidad se van a dejar para
    //histórico d edatos (formar gráficas y eso).

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String pais; 

    @NotNull
    @Positive
    @Column(nullable = false)
    private Double precioKwh; // en €/kWh

    public Long getId() {
        return id;
    }

    public String getPais() {
        return pais;
    }

    public Double getPrecioKwh() {
        return precioKwh;
    }

}
