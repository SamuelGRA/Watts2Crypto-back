package com.watts2crypto.watts2crypto_backend.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class AsicTest {

    @Test
    void constructorAndAlgorithmAccessorsWork() {
        Map<String, RendimientoAlgoritmo> algorithms = new HashMap<>();
        algorithms.put("sha256", new RendimientoAlgoritmo(110_000_000_000.0, 3250));

        Asic asic = new Asic("Antminer S19", 3250, 110.0, algorithms);

        assertEquals("Antminer S19", asic.getNombre());
        assertEquals(3250, asic.getConsumoNominal());
        assertEquals(110.0, asic.getHashrateNominal());
        assertEquals(algorithms, asic.getAlgorithms());
    }

    @Test
    void algorithmLookupReturnsNullWhenMissingData() {
        Asic asic = new Asic();

        assertNull(asic.getSpeedHashesPorSegundo(null));
        assertNull(asic.getSpeedGigahashesPorSegundo(null));
        assertNull(asic.getConsumoEnWatts(null));

        asic.setAlgorithms(null);

        assertNull(asic.getSpeedHashesPorSegundo("sha256"));
        assertNull(asic.getSpeedGigahashesPorSegundo("sha256"));
        assertNull(asic.getConsumoEnWatts("sha256"));

        asic.setAlgorithms(Map.of("kawpow", new RendimientoAlgoritmo(100.0, 200)));

        assertNull(asic.getSpeedHashesPorSegundo("sha256"));
        assertNull(asic.getSpeedGigahashesPorSegundo("sha256"));
        assertNull(asic.getConsumoEnWatts("sha256"));
    }

    @Test
    void algorithmLookupReturnsValuesAndConvertsToGigahashes() {
        Asic asic = new Asic();
        asic.setAlgorithms(Map.of("sha256", new RendimientoAlgoritmo(110_000_000_000.0, 3250)));

        assertEquals(110_000_000_000.0, asic.getSpeedHashesPorSegundo("sha256"));
        assertEquals(110.0, asic.getSpeedGigahashesPorSegundo("sha256"));
        assertEquals(3250, asic.getConsumoEnWatts("sha256"));
    }

    @Test
    void setAlgorithmsReplacesTheInternalMap() {
        Asic asic = new Asic();
        Map<String, RendimientoAlgoritmo> algorithms = Map.of("ethash", new RendimientoAlgoritmo(123.0, 150));

        asic.setAlgorithms(algorithms);

        assertEquals(algorithms, asic.getAlgorithms());
    }
}