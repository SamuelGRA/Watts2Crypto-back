package com.watts2crypto.watts2crypto_backend.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class GpuTest {

    @Test
    void constructorAndAlgorithmAccessorsWork() {
        Map<String, RendimientoAlgoritmo> algorithms = new HashMap<>();
        algorithms.put("ethash", new RendimientoAlgoritmo(123.0, 220));

        Gpu gpu = new Gpu("RTX 3080", 320, 1000.0, algorithms);

        assertEquals("RTX 3080", gpu.getNombre());
        assertEquals(320, gpu.getConsumoNominal());
        assertEquals(1000.0, gpu.getHashrateNominal());
        assertEquals(algorithms, gpu.getAlgorithms());
    }

    @Test
    void algorithmLookupReturnsNullWhenDataIsMissing() {
        Gpu gpu = new Gpu();

        assertNull(gpu.getSpeedHashesPorSegundo(null));
        assertNull(gpu.getConsumoEnWatts(null));

        gpu.setAlgorithms(null);

        assertNull(gpu.getSpeedHashesPorSegundo("ethash"));
        assertNull(gpu.getConsumoEnWatts("ethash"));

        gpu.setAlgorithms(Map.of("kawpow", new RendimientoAlgoritmo(90.0, 200)));

        assertNull(gpu.getSpeedHashesPorSegundo("ethash"));
        assertNull(gpu.getConsumoEnWatts("ethash"));
    }

    @Test
    void algorithmLookupReturnsValuesForKnownAlgorithm() {
        Gpu gpu = new Gpu();
        gpu.setAlgorithms(Map.of("ethash", new RendimientoAlgoritmo(123.0, 220)));

        assertEquals(123.0, gpu.getSpeedHashesPorSegundo("ethash"));
        assertEquals(220, gpu.getConsumoEnWatts("ethash"));
    }

    @Test
    void setAlgorithmsReplacesTheInternalMap() {
        Gpu gpu = new Gpu();
        Map<String, RendimientoAlgoritmo> algorithms = Map.of("ethash", new RendimientoAlgoritmo(123.0, 220));

        gpu.setAlgorithms(algorithms);

        assertEquals(algorithms, gpu.getAlgorithms());
    }
}