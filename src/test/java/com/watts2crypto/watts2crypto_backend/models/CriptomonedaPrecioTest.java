package com.watts2crypto.watts2crypto_backend.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class CriptomonedaPrecioTest {

    @Test
    void constructorAndGettersWork() {
        Criptomoneda bitcoin = new Criptomoneda(1L, "bitcoin", "BTC", "Bitcoin");
        CriptomonedaPrecio precio = new CriptomonedaPrecio(BigDecimal.valueOf(45000.0), LocalDate.of(2026, 5, 31), bitcoin);

        assertEquals(BigDecimal.valueOf(45000.0), precio.getPrecioEur());
        assertEquals(LocalDate.of(2026, 5, 31), precio.getFecha());
        assertEquals(bitcoin, precio.getCriptomoneda());
    }

    @Test
    void settersUpdateAllFields() {
        CriptomonedaPrecio precio = new CriptomonedaPrecio();
        Criptomoneda bitcoin = new Criptomoneda(1L, "bitcoin", "BTC", "Bitcoin");

        precio.setId(7L);
        precio.setPrecioUsd(BigDecimal.valueOf(47000.0));
        precio.setFecha(LocalDate.of(2026, 6, 1));
        precio.setCriptomoneda(bitcoin);

        assertEquals(7L, precio.getId());
        assertEquals(BigDecimal.valueOf(47000.0), precio.getPrecioEur());
        assertEquals(LocalDate.of(2026, 6, 1), precio.getFecha());
        assertEquals(bitcoin, precio.getCriptomoneda());
    }
}