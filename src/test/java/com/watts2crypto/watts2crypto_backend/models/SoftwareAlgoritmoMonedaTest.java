package com.watts2crypto.watts2crypto_backend.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

class SoftwareAlgoritmoMonedaTest {

    @Test
    void constructorAndAccessorsWork() {
        Software software = new Software("Miner X", Set.of(Software.HardwareUsable.GPU), Set.of(Software.SistemaOperativo.LINUX), "miner-x");

        SoftwareAlgoritmoMoneda detalle = new SoftwareAlgoritmoMoneda(software, "BTC", "SHA-256", 2.0);

        assertSame(software, detalle.getSoftware());
        assertEquals("BTC", detalle.getMoneda());
        assertEquals("SHA-256", detalle.getAlgoritmo());
        assertEquals(2.0, detalle.getComision());
    }

    @Test
    void settersReplaceAllFields() {
        Software software = new Software("Miner Y", Set.of(Software.HardwareUsable.CPU), Set.of(Software.SistemaOperativo.WINDOWS), "miner-y");
        SoftwareAlgoritmoMoneda detalle = new SoftwareAlgoritmoMoneda();

        detalle.setId(11L);
        detalle.setSoftware(software);
        detalle.setMoneda("ETH");
        detalle.setAlgoritmo("Ethash");
        detalle.setComision(3.5);

        assertEquals(11L, detalle.getId());
        assertSame(software, detalle.getSoftware());
        assertEquals("ETH", detalle.getMoneda());
        assertEquals("Ethash", detalle.getAlgoritmo());
        assertEquals(3.5, detalle.getComision());
    }

    @Test
    void addDetalleAlgoritmoMonedaLinksBackToSoftware() {
        Software software = new Software("Miner Z", Set.of(Software.HardwareUsable.ASIC), Set.of(Software.SistemaOperativo.LINUX), "miner-z");
        SoftwareAlgoritmoMoneda detalle = new SoftwareAlgoritmoMoneda(null, "XMR", "RandomX", 1.25);

        software.addDetalleAlgoritmoMoneda(detalle);

        assertSame(software, detalle.getSoftware());
        assertEquals(List.of(detalle), software.getDetallesAlgoritmoMoneda());
    }
}