package com.watts2crypto.watts2crypto_backend.models.DTOs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CalculoOutputDtoTest {

    @Test
    void compactConstructorPopulatesSummaryFields() {
        CalculoOutputDto dto = new CalculoOutputDto(10.0, 300.0, 3650.0, 42.0);

        assertEquals(10.0, dto.getBeneficioDiario());
        assertEquals(300.0, dto.getBeneficioMensual());
        assertEquals(3650.0, dto.getBeneficioAnual());
        assertEquals(42.0, dto.getRoiDias());
    }

    @Test
    void fullConstructorPopulatesDetailedFields() {
        CalculoOutputDto dto = new CalculoOutputDto(
                10.0,
                12.0,
                300.0,
                3650.0,
                42.0,
                123.0,
                456.0,
                0.2,
                1.5,
                2.3,
                "kawpow");

        assertEquals(10.0, dto.getBeneficioDiario());
        assertEquals(12.0, dto.getBeneficioBrutoDiario());
        assertEquals(300.0, dto.getBeneficioMensual());
        assertEquals(3650.0, dto.getBeneficioAnual());
        assertEquals(42.0, dto.getRoiDias());
        assertEquals(123.0, dto.getHashrate());
        assertEquals(456.0, dto.getConsumoW());
        assertEquals(0.2, dto.getPrecioKwh());
        assertEquals(1.5, dto.getComision());
        assertEquals(2.3, dto.getCosteEnergiaDiario());
        assertEquals("kawpow", dto.getAlgoritmoUsado());
    }

    @Test
    void settersUpdateAllFields() {
        CalculoOutputDto dto = new CalculoOutputDto();

        dto.setBeneficioDiario(11.0);
        dto.setBeneficioBrutoDiario(13.0);
        dto.setBeneficioMensual(310.0);
        dto.setBeneficioAnual(3720.0);
        dto.setRoiDias(40.0);
        dto.setHashrate(999.0);
        dto.setConsumoW(500.0);
        dto.setPrecioKwh(0.15);
        dto.setComision(2.0);
        dto.setCosteEnergiaDiario(3.5);
        dto.setAlgoritmoUsado("sha256");

        assertEquals(11.0, dto.getBeneficioDiario());
        assertEquals(13.0, dto.getBeneficioBrutoDiario());
        assertEquals(310.0, dto.getBeneficioMensual());
        assertEquals(3720.0, dto.getBeneficioAnual());
        assertEquals(40.0, dto.getRoiDias());
        assertEquals(999.0, dto.getHashrate());
        assertEquals(500.0, dto.getConsumoW());
        assertEquals(0.15, dto.getPrecioKwh());
        assertEquals(2.0, dto.getComision());
        assertEquals(3.5, dto.getCosteEnergiaDiario());
        assertEquals("sha256", dto.getAlgoritmoUsado());
    }
}