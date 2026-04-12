package com.watts2crypto.watts2crypto_backend.models.DTOs;

public class CalculoHardwareItemDto {

    private String tipoHardware;
    private String nombreHardware;
    private Integer cantidad;

    public CalculoHardwareItemDto() {
    }

    public CalculoHardwareItemDto(String tipoHardware, String nombreHardware, Integer cantidad) {
        this.tipoHardware = tipoHardware;
        this.nombreHardware = nombreHardware;
        this.cantidad = cantidad;
    }

    public String getTipoHardware() {
        return tipoHardware;
    }

    public void setTipoHardware(String tipoHardware) {
        this.tipoHardware = tipoHardware;
    }

    public String getNombreHardware() {
        return nombreHardware;
    }

    public void setNombreHardware(String nombreHardware) {
        this.nombreHardware = nombreHardware;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}