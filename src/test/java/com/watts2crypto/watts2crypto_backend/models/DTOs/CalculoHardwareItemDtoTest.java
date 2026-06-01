package com.watts2crypto.watts2crypto_backend.models.DTOs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CalculoHardwareItemDtoTest {

    @Test
    void constructorAndGettersWork() {
        CalculoHardwareItemDto dto = new CalculoHardwareItemDto("GPU", "RTX 3080", 2);

        assertEquals("GPU", dto.getTipoHardware());
        assertEquals("RTX 3080", dto.getNombreHardware());
        assertEquals(2, dto.getCantidad());
    }

    @Test
    void settersUpdateFields() {
        CalculoHardwareItemDto dto = new CalculoHardwareItemDto();

        dto.setTipoHardware("ASIC");
        dto.setNombreHardware("Antminer S19");
        dto.setCantidad(3);

        assertEquals("ASIC", dto.getTipoHardware());
        assertEquals("Antminer S19", dto.getNombreHardware());
        assertEquals(3, dto.getCantidad());
    }
}