package com.watts2crypto.watts2crypto_backend.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class SimbolosISOMonedasTest {

    @Test
    void constructorAndAccessorsWork() {
        SimbolosISOMonedas simbolo = new SimbolosISOMonedas("USD");

        assertEquals("USD", simbolo.getSimbolo());
    }

    @Test
    void settersReplaceFields() {
        SimbolosISOMonedas simbolo = new SimbolosISOMonedas();

        simbolo.setId(9L);
        simbolo.setSimbolo("EUR");

        assertSame(9L, simbolo.getId());
        assertEquals("EUR", simbolo.getSimbolo());
    }
}