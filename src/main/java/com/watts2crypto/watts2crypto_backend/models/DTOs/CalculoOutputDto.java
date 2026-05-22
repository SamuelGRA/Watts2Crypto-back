package com.watts2crypto.watts2crypto_backend.models.DTOs;

public class CalculoOutputDto {

    private Double beneficioDiario;
    private Double beneficioBrutoDiario;
    private Double beneficioMensual;
    private Double beneficioAnual;
    private Double roiDias;
    private Double hashrate;
    private Double consumoW;
    private Double precioKwh;
    private Double comision;
    private Double costeEnergiaDiario;
    private String algoritmoUsado;

    public CalculoOutputDto() {
    }

    public CalculoOutputDto(Double beneficioDiario, Double beneficioMensual, Double beneficioAnual, Double roiDias) {
        this.beneficioDiario = beneficioDiario;
        this.beneficioMensual = beneficioMensual;
        this.beneficioAnual = beneficioAnual;
        this.roiDias = roiDias;
    }

    public CalculoOutputDto(Double beneficioDiario, Double beneficioBrutoDiario, Double beneficioMensual, Double beneficioAnual, Double roiDias,
            Double hashrate, Double consumoW, Double precioKwh, Double comision, Double costeEnergiaDiario, String algoritmoUsado) {
        this.beneficioDiario = beneficioDiario;
        this.beneficioBrutoDiario = beneficioBrutoDiario;
        this.beneficioMensual = beneficioMensual;
        this.beneficioAnual = beneficioAnual;
        this.roiDias = roiDias;
        this.hashrate = hashrate;
        this.consumoW = consumoW;
        this.precioKwh = precioKwh;
        this.comision = comision;
        this.costeEnergiaDiario = costeEnergiaDiario;
        this.algoritmoUsado = algoritmoUsado;
    }

    public Double getBeneficioDiario() {
        return beneficioDiario;
    }

    public void setBeneficioDiario(Double beneficioDiario) {
        this.beneficioDiario = beneficioDiario;
    }

    public Double getBeneficioMensual() {
        return beneficioMensual;
    }

    public void setBeneficioMensual(Double beneficioMensual) {
        this.beneficioMensual = beneficioMensual;
    }

    public Double getBeneficioAnual() {
        return beneficioAnual;
    }

    public void setBeneficioAnual(Double beneficioAnual) {
        this.beneficioAnual = beneficioAnual;
    }

    public Double getRoiDias() {
        return roiDias;
    }

    public void setRoiDias(Double roiDias) {
        this.roiDias = roiDias;
    }

    public Double getHashrate() {
        return hashrate;
    }

    public void setHashrate(Double hashrate) {
        this.hashrate = hashrate;
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

    public String getAlgoritmoUsado() {
        return algoritmoUsado;
    }

    public void setAlgoritmoUsado(String algoritmoUsado) {
        this.algoritmoUsado = algoritmoUsado;
    }

    public Double getBeneficioBrutoDiario() {
        return beneficioBrutoDiario;
    }

    public void setBeneficioBrutoDiario(Double beneficioBrutoDiario) {
        this.beneficioBrutoDiario = beneficioBrutoDiario;
    }

    public Double getCosteEnergiaDiario() {
        return costeEnergiaDiario;
    }

    public void setCosteEnergiaDiario(Double costeEnergiaDiario) {
        this.costeEnergiaDiario = costeEnergiaDiario;
    }

}