package com.watts2crypto.watts2crypto_backend.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

class PoolMonedaComisionTest {

    @Test
    void constructorAndAccessorsWork() {
        Pool pool = new Pool("Pool A", Set.of(Pool.EsquemaDePago.PPS), List.of("EU"), "pool-a");

        PoolMonedaComision detalle = new PoolMonedaComision(pool, "BTC", 1.5);

        assertSame(pool, detalle.getPool());
        assertEquals("BTC", detalle.getMoneda());
        assertEquals(1.5, detalle.getComision());
    }

    @Test
    void settersReplaceAllFields() {
        Pool pool = new Pool("Pool B", Set.of(Pool.EsquemaDePago.PPLNS), List.of("US"), "pool-b");
        PoolMonedaComision detalle = new PoolMonedaComision();

        detalle.setId(7L);
        detalle.setPool(pool);
        detalle.setMoneda("XMR");
        detalle.setComision(2.75);

        assertEquals(7L, detalle.getId());
        assertSame(pool, detalle.getPool());
        assertEquals("XMR", detalle.getMoneda());
        assertEquals(2.75, detalle.getComision());
    }
}