package com.watts2crypto.watts2crypto_backend.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

class DirectRequestAvailabilityServiceTest {

    @Test
    void availabilityDependsOnApiKeyPresence() {
        DirectRequestAvailabilityService disabled = new DirectRequestAvailabilityService("");
        DirectRequestAvailabilityService enabled = new DirectRequestAvailabilityService("secret-token");

        assertFalse(disabled.isDirectCryptoAvailable());
        assertTrue(enabled.isDirectCryptoAvailable());
    }

    @Test
    void assertDirectCryptoAvailableFailsWhenNoKeyIsConfigured() {
        DirectRequestAvailabilityService service = new DirectRequestAvailabilityService("   ");

        assertThrows(ResponseStatusException.class, service::assertDirectCryptoAvailable);
    }
}