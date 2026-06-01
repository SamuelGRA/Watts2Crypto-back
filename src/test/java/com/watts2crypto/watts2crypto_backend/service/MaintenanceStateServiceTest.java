package com.watts2crypto.watts2crypto_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

class MaintenanceStateServiceTest {

    @Test
    void beginAndEndRefreshUpdatesState() {
        MaintenanceStateService service = new MaintenanceStateService();

        service.beginRefresh("software");

        assertTrue(service.isRefreshing());
        assertEquals(List.of("software"), service.getActiveRefreshes());

        service.endRefresh("software");

        assertFalse(service.isRefreshing());
        assertEquals(List.of(), service.getActiveRefreshes());
    }

    @Test
    void endRefreshNeverDropsBelowZero() {
        MaintenanceStateService service = new MaintenanceStateService();

        service.endRefresh("missing");

        assertFalse(service.isRefreshing());
        assertEquals(List.of(), service.getActiveRefreshes());
    }
}