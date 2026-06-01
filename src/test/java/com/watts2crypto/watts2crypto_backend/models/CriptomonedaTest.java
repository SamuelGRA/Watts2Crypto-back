package com.watts2crypto.watts2crypto_backend.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

class CriptomonedaTest {

    @Test
    void constructorAndGettersExposeFields() {
        Criptomoneda bitcoin = new Criptomoneda(1L, "bitcoin", "BTC", "Bitcoin");

        assertEquals(1L, bitcoin.getId());
        assertEquals("bitcoin", bitcoin.getAssetId());
        assertEquals("BTC", bitcoin.getSimbolo());
        assertEquals("Bitcoin", bitcoin.getNombre());
        assertTrue(bitcoin.getHistoricoPrecios().isEmpty());
    }

    @Test
    void settersUpdateFieldsAndLinkHistoricalPricesBackToTheCrypto() {
        Criptomoneda bitcoin = new Criptomoneda();
        CriptomonedaPrecio precioUno = new CriptomonedaPrecio(BigDecimal.valueOf(45000.0), LocalDate.of(2026, 5, 30), bitcoin);
        CriptomonedaPrecio precioDos = new CriptomonedaPrecio(BigDecimal.valueOf(46000.0), LocalDate.of(2026, 5, 31), bitcoin);

        bitcoin.setId(7L);
        bitcoin.setAssetId("bitcoin");
        bitcoin.setSimbolo("BTC");
        bitcoin.setNombre("Bitcoin");
        bitcoin.setHistoricoPrecios(List.of(precioUno, precioDos));

        assertEquals(7L, bitcoin.getId());
        assertEquals("bitcoin", bitcoin.getAssetId());
        assertEquals("BTC", bitcoin.getSimbolo());
        assertEquals("Bitcoin", bitcoin.getNombre());
        assertEquals(2, bitcoin.getHistoricoPrecios().size());
        assertSame(bitcoin, precioUno.getCriptomoneda());
        assertSame(bitcoin, precioDos.getCriptomoneda());
        assertEquals(BigDecimal.valueOf(45000.0), bitcoin.getHistoricoPrecios().get(0).getPrecioEur());
        assertEquals(BigDecimal.valueOf(46000.0), bitcoin.getHistoricoPrecios().get(1).getPrecioEur());
        assertNull(bitcoin.getHistoricoPrecios().get(0).getId());
    }
}