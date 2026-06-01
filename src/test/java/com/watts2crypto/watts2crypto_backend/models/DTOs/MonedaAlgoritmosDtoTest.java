package com.watts2crypto.watts2crypto_backend.models.DTOs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

class MonedaAlgoritmosDtoTest {

    @Test
    void constructorCopiesAlgorithmsAndPreservesInsertionOrder() {
        Set<String> algoritmos = new LinkedHashSet<>(Set.of("SHA-256", "KawPow"));

        MonedaAlgoritmosDto dto = new MonedaAlgoritmosDto("Bitcoin", algoritmos);

        assertEquals("Bitcoin", dto.getNombre());
        assertEquals(new LinkedHashSet<>(Set.of("SHA-256", "KawPow")), dto.getAlgoritmos());
        assertNotSame(algoritmos, dto.getAlgoritmos());
    }

    @Test
    void constructorCreatesEmptySetWhenAlgorithmsAreNull() {
        MonedaAlgoritmosDto dto = new MonedaAlgoritmosDto("Bitcoin", null);

        assertEquals("Bitcoin", dto.getNombre());
        assertEquals(Set.of(), dto.getAlgoritmos());
    }

    @Test
    void settersReplaceFieldsAndCopyAlgorithms() {
        MonedaAlgoritmosDto dto = new MonedaAlgoritmosDto();
        Set<String> algoritmos = new LinkedHashSet<>(Set.of("Ethash", "RandomX"));

        dto.setNombre("Ethereum");
        dto.setAlgoritmos(algoritmos);

        assertEquals("Ethereum", dto.getNombre());
        assertEquals(new LinkedHashSet<>(Set.of("Ethash", "RandomX")), dto.getAlgoritmos());
        assertNotSame(algoritmos, dto.getAlgoritmos());
    }

    @Test
    void setAlgoritmosCreatesEmptySetWhenNull() {
        MonedaAlgoritmosDto dto = new MonedaAlgoritmosDto();

        dto.setAlgoritmos(null);

        assertEquals(Set.of(), dto.getAlgoritmos());
    }
}