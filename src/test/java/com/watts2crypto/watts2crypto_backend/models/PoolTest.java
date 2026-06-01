package com.watts2crypto.watts2crypto_backend.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

class PoolTest {

    @Test
    void constructorAndGettersExposeFields() {
        Pool pool = new Pool(
                "Poolin",
                Set.of(Pool.EsquemaDePago.PPS, Pool.EsquemaDePago.FPPS),
                List.of("EU", "US"),
                "poolin");

        assertEquals("Poolin", pool.getNombre());
        assertEquals(Set.of(Pool.EsquemaDePago.PPS, Pool.EsquemaDePago.FPPS), pool.getEsquemaDePago());
        assertEquals(List.of("EU", "US"), pool.getRegiones());
        assertEquals("poolin", pool.getHashrateSlug());
        assertTrue(pool.getDetallesMonedaComision().isEmpty());
    }

    @Test
    void settersUpdateFieldsAndAddDetalleLinksBackToPool() {
        Pool pool = new Pool();
        PoolMonedaComision detalle = new PoolMonedaComision();

        pool.setId(7L);
        pool.setNombre("F2Pool");
        pool.setEsquemaDePago(Set.of(Pool.EsquemaDePago.PPLNS));
        pool.setRegiones(new ArrayList<>(List.of("EU")));
        pool.setHashrateSlug("f2pool");
        pool.addDetalleMonedaComision(detalle);

        assertEquals(7L, pool.getId());
        assertEquals("F2Pool", pool.getNombre());
        assertEquals(Set.of(Pool.EsquemaDePago.PPLNS), pool.getEsquemaDePago());
        assertEquals(List.of("EU"), pool.getRegiones());
        assertEquals("f2pool", pool.getHashrateSlug());
        assertEquals(1, pool.getDetallesMonedaComision().size());
        assertSame(pool, detalle.getPool());
    }
}