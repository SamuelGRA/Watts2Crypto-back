package com.watts2crypto.watts2crypto_backend.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class HardwareTest {

    private static class TestHardware extends Hardware {
        TestHardware() {
            super();
        }

        TestHardware(String nombre, Integer consumoNominal, Double hashrateNominal) {
            super(nombre, consumoNominal, hashrateNominal);
        }
    }

    @Test
    void constructorAndGettersExposeBaseFields() {
        TestHardware hardware = new TestHardware("RTX 4090", 450, 100.5);

        assertNull(hardware.getId());
        assertEquals("RTX 4090", hardware.getNombre());
        assertEquals(450, hardware.getConsumoNominal());
        assertEquals(100.5, hardware.getHashrateNominal());
    }

    @Test
    void settersUpdateBaseFields() {
        TestHardware hardware = new TestHardware();

        hardware.setId(12L);
        hardware.setNombre("Antminer S19");
        hardware.setConsumoNominal(3250);
        hardware.setHashrateNominal(95.0);

        assertEquals(12L, hardware.getId());
        assertEquals("Antminer S19", hardware.getNombre());
        assertEquals(3250, hardware.getConsumoNominal());
        assertEquals(95.0, hardware.getHashrateNominal());
    }
}