package com.watts2crypto.watts2crypto_backend.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class SoftwareTest {

    @Test
    void constructorAndGettersExposeFields() {
        Software software = new Software(
                "PhoenixMiner",
                Set.of(Software.HardwareUsable.GPU, Software.HardwareUsable.ASIC),
                Set.of(Software.SistemaOperativo.WINDOWS, Software.SistemaOperativo.LINUX),
                "phoenixminer");

        assertEquals("PhoenixMiner", software.getNombre());
        assertEquals(Set.of(Software.HardwareUsable.GPU, Software.HardwareUsable.ASIC), software.getHardwareUsable());
        assertEquals(Set.of(Software.SistemaOperativo.WINDOWS, Software.SistemaOperativo.LINUX), software.getSistemas());
        assertEquals("phoenixminer", software.getHashrateSlug());
        assertTrue(software.getDetallesAlgoritmoMoneda().isEmpty());
        assertNull(software.getId());
    }

    @Test
    void settersUpdateFieldsAndAddDetalleLinksBackToSoftware() {
        Software software = new Software();
        SoftwareAlgoritmoMoneda detalle = new SoftwareAlgoritmoMoneda();

        ReflectionTestUtils.setField(software, "id", 9L);
        assertEquals(9L, software.setId());

        software.setNombre("BzMiner");
        software.setHardwareUsable(Set.of(Software.HardwareUsable.CPU, Software.HardwareUsable.GPU));
        software.setSistemas(Set.of(Software.SistemaOperativo.LINUX));
        software.setHashrateSlug("bzminer");
        software.addDetalleAlgoritmoMoneda(detalle);

        assertEquals("BzMiner", software.getNombre());
        assertEquals(Set.of(Software.HardwareUsable.CPU, Software.HardwareUsable.GPU), software.getHardwareUsable());
        assertEquals(Set.of(Software.SistemaOperativo.LINUX), software.getSistemas());
        assertEquals("bzminer", software.getHashrateSlug());
        assertEquals(1, software.getDetallesAlgoritmoMoneda().size());
        assertSame(software, detalle.getSoftware());
    }
}