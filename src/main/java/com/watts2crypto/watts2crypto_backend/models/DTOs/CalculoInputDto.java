package com.watts2crypto.watts2crypto_backend.models.DTOs;

public class CalculoInputDto {

    private Double hashrate;
    private String moneda;
    private Double consumoW;
    private Double precioKwh;
    private Double comision;
    private String tipoHardware;
    private String nombreHardware;
    private String algoritmo;
    private String pais;
    private String pool;
    private String software;
    private Double costoInicialHardware;

    public CalculoInputDto() {
    }

    public CalculoInputDto(Double hashrate, String moneda, Double consumoW, Double precioKwh, Double comision,
            String tipoHardware, String nombreHardware, String algoritmo, String pais, String pool, String software,
            Double costoInicialHardware) {
        this.hashrate = hashrate;
        this.moneda = moneda;
        this.consumoW = consumoW;
        this.precioKwh = precioKwh;
        this.comision = comision;
        this.tipoHardware = tipoHardware;
        this.nombreHardware = nombreHardware;
        this.algoritmo = algoritmo;
        this.pais = pais;
        this.pool = pool;
        this.software = software;
        this.costoInicialHardware = costoInicialHardware;
    }

    public Double getHashrate() {
        return hashrate;
    }

    public void setHashrate(Double hashrate) {
        this.hashrate = hashrate;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public Double getConsumoW() {
        return consumoW;
    }

    public void setConsumoW(Double consumoW) {
        this.consumoW = consumoW;
    }

    public Double getPrecioKwh() {
        return precioKwh;
    }

    public void setPrecioKwh(Double precioKwh) {
        this.precioKwh = precioKwh;
    }

    public Double getComision() {
        return comision;
    }

    public void setComision(Double comision) {
        this.comision = comision;
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

    public String getAlgoritmo() {
        return algoritmo;
    }

    public void setAlgoritmo(String algoritmo) {
        this.algoritmo = algoritmo;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getPool() {
        return pool;
    }

    public void setPool(String pool) {
        this.pool = pool;
    }

    public String getSoftware() {
        return software;
    }

    public void setSoftware(String software) {
        this.software = software;
    }

    public Double getCostoInicialHardware() {
        return costoInicialHardware;
    }

    public void setCostoInicialHardware(Double costoInicialHardware) {
        this.costoInicialHardware = costoInicialHardware;
    }

}

