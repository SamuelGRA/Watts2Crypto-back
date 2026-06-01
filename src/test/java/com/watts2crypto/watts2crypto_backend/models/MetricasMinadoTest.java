package com.watts2crypto.watts2crypto_backend.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class MetricasMinadoTest {

    @Test
    void constructorAndGettersExposeAllFields() {
        MetricasMinado metrica = new MetricasMinado(
                10,
                "Bitcoin",
                "SHA-256",
                123456L,
                600.0,
                3.125,
                45000.0);

        assertNull(metrica.getId());
        assertEquals(10, metrica.getWhatToMineCoinId());
        assertEquals("Bitcoin", metrica.getNombreMoneda());
        assertEquals("SHA-256", metrica.getAlgoritmo());
        assertEquals(123456L, metrica.getNethash());
        assertEquals(600.0, metrica.getBlockTimeSegundos());
        assertEquals(3.125, metrica.getBlockReward());
        assertEquals(45000.0, metrica.getPrecioActualEur());
    }

    @Test
    void settersUpdateFields() {
        MetricasMinado metrica = new MetricasMinado();

        metrica.setId(5L);
        metrica.setWhatToMineCoinId(42);
        metrica.setNombreMoneda("Litecoin");
        metrica.setAlgoritmo("Scrypt");
        metrica.setNethash(999L);
        metrica.setBlockTimeSegundos(150.0);
        metrica.setBlockReward(12.5);
        metrica.setPrecioActualEur(85.75);

        assertEquals(5L, metrica.getId());
        assertEquals(42, metrica.getWhatToMineCoinId());
        assertEquals("Litecoin", metrica.getNombreMoneda());
        assertEquals("Scrypt", metrica.getAlgoritmo());
        assertEquals(999L, metrica.getNethash());
        assertEquals(150.0, metrica.getBlockTimeSegundos());
        assertEquals(12.5, metrica.getBlockReward());
        assertEquals(85.75, metrica.getPrecioActualEur());
    }
}