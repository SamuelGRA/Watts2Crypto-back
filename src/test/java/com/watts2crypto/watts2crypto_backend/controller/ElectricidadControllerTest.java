package com.watts2crypto.watts2crypto_backend.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.web.servlet.MockMvc;

import com.watts2crypto.watts2crypto_backend.models.Electricidad;
import com.watts2crypto.watts2crypto_backend.service.ElectricidadService;

@WebMvcTest(ElectricidadController.class)
@AutoConfigureMockMvc(addFilters = false)
class ElectricidadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ElectricidadService service;

    @MockitoBean
    private RestTemplateBuilder restTemplateBuilder;

    @Test
    void returnsAllElectricityPrices() throws Exception {
        when(service.findAll()).thenReturn(List.of(new Electricidad("ES", LocalDateTime.of(2026, 5, 31, 12, 0), 120.5)));

        mockMvc.perform(get("/api/electricidad"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].zona").value("ES"))
                .andExpect(jsonPath("$[0].precioMwh").value(120.5));
    }

    @Test
    void returnsPricesByZoneAndDateRange() throws Exception {
        when(service.findByDateRangeAndZone("ES", LocalDateTime.of(2026, 5, 1, 0, 0),
                LocalDateTime.of(2026, 5, 2, 0, 0))).thenReturn(List.of(
                        new Electricidad("ES", LocalDateTime.of(2026, 5, 1, 12, 0), 118.0)));

        mockMvc.perform(get("/api/electricidad/ES/historico")
                .param("start", "2026-05-01T00:00:00")
                .param("end", "2026-05-02T00:00:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].precioMwh").value(118.0));
    }

    @Test
    void returnsDirectZoneSearch() throws Exception {
        when(service.findElectricidadDirectaPorZona("ES")).thenReturn(List.of(
                new Electricidad("ES", LocalDateTime.of(2026, 5, 31, 12, 0), 120.5)));

        mockMvc.perform(get("/api/electricidad/ES/busquedaDirecta"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].zona").value("ES"));
    }

    @Test
    void returnsExactPriceForZoneAndDate() throws Exception {
        when(service.findPriceByExactDateAndZone("ES", LocalDateTime.of(2026, 5, 31, 12, 0))).thenReturn(120.5);

        mockMvc.perform(get("/api/electricidad/ES/2026-05-31T12:00:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(120.5));
    }

    @Test
    void returnsLatestPriceForZone() throws Exception {
        when(service.findLatestPriceInZone("ES")).thenReturn(new Electricidad("ES", LocalDateTime.of(2026, 5, 31, 12, 0), 120.5));

        mockMvc.perform(get("/api/electricidad/ES/masReciente"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.precioMwh").value(120.5));
    }

    @Test
    void returnsAllZoneNames() throws Exception {
        when(service.findAllZones()).thenReturn(List.of("ES", "FR"));

        mockMvc.perform(get("/api/electricidad/zonas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("ES"))
                .andExpect(jsonPath("$[1]").value("FR"));
    }

    @Test
    void returnsAllZonesForDirectSearch() throws Exception {
        when(service.findZonesForDirectSearch()).thenReturn(List.of("ES", "PT"));

        mockMvc.perform(get("/api/electricidad/zonasDirecta"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("ES"))
                .andExpect(jsonPath("$[1]").value("PT"));
    }
}