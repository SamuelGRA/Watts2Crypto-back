package com.watts2crypto.watts2crypto_backend.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.web.servlet.MockMvc;

import com.watts2crypto.watts2crypto_backend.models.MonedaTradicional;
import com.watts2crypto.watts2crypto_backend.service.MonedaTradicionalService;

@WebMvcTest(MonedaTradicionalController.class)
@AutoConfigureMockMvc(addFilters = false)
class MonedaTradicionalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MonedaTradicionalService service;

        @MockitoBean
        private RestTemplateBuilder restTemplateBuilder;

    @Test
    void returnsAllMonedasTradicionales() throws Exception {
        when(service.findAllMonedasTradicionales()).thenReturn(List.of(
                new MonedaTradicional("EUR", "USD", 1.08, LocalDate.of(2026, 5, 31))));

        mockMvc.perform(get("/api/fiat"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].monedaBase").value("EUR"))
                .andExpect(jsonPath("$[0].monedaObjetivo").value("USD"));
    }

    @Test
    void returnsAllSymbols() throws Exception {
        when(service.findAllSymbols()).thenReturn(List.of("EUR", "USD"));

        mockMvc.perform(get("/api/fiat/simbolos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("EUR"))
                .andExpect(jsonPath("$[1]").value("USD"));
    }

    @Test
    void returnsExchangeRateByBaseAndTarget() throws Exception {
        when(service.findTasaCambioActualPorBaseYObjetivo("EUR", "USD"))
                .thenReturn(new MonedaTradicional("EUR", "USD", 1.08, LocalDate.of(2026, 5, 31)));

        mockMvc.perform(get("/api/fiat/tasaCambio/EUR/USD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tasaCambio").value(1.08));
    }

    @Test
    void returnsHistoricalExchangeRates() throws Exception {
        when(service.findHistoricoPorBaseObjetivoYRangoFechas("EUR", "USD", LocalDate.of(2026, 5, 1),
                LocalDate.of(2026, 5, 3))).thenReturn(List.of(
                        new MonedaTradicional("EUR", "USD", 1.07, LocalDate.of(2026, 5, 1)),
                        new MonedaTradicional("EUR", "USD", 1.08, LocalDate.of(2026, 5, 2))));

        mockMvc.perform(get("/api/fiat/historico/EUR/USD")
                .param("start", "2026-05-01")
                .param("end", "2026-05-03"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fecha").value("2026-05-01"))
                .andExpect(jsonPath("$[1].tasaCambio").value(1.08));
    }

    @Test
    void convertsAmount() throws Exception {
        when(service.convertirCantidad(10.0, "EUR", "USD")).thenReturn(10.8);

        mockMvc.perform(get("/api/fiat/convertirCantidad/10")
                .param("monedaOrigen", "EUR")
                .param("monedaObjetivo", "USD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(10.8));
    }
}