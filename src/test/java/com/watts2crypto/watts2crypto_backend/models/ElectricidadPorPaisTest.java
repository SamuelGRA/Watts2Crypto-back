package com.watts2crypto.watts2crypto_backend.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class ElectricidadPorPaisTest {

    @Test
    void gettersExposePrivateFields() {
        ElectricidadPorPais precio = new ElectricidadPorPais();

        ReflectionTestUtils.setField(precio, "id", 1L);
        ReflectionTestUtils.setField(precio, "pais", "ES");
        ReflectionTestUtils.setField(precio, "precioKwh", 0.25);

        assertEquals(1L, precio.getId());
        assertEquals("ES", precio.getPais());
        assertEquals(0.25, precio.getPrecioKwh());
    }
}