package com.watts2crypto.watts2crypto_backend.models.DTOs;

public class CalculoOutputDto {

    private Double beneficioDiario;
    private Double beneficioMensual;
    private Double beneficioAnual;
    private Double roiDias;

    public CalculoOutputDto() {
    }

    public CalculoOutputDto(Double beneficioDiario, Double beneficioMensual, Double beneficioAnual, Double roiDias) {
        this.beneficioDiario = beneficioDiario;
        this.beneficioMensual = beneficioMensual;
        this.beneficioAnual = beneficioAnual;
        this.roiDias = roiDias;
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

}