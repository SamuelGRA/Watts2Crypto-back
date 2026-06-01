package com.watts2crypto.watts2crypto_backend.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.web.servlet.MockMvc;

import com.watts2crypto.watts2crypto_backend.models.Criptomoneda;
import com.watts2crypto.watts2crypto_backend.models.CriptomonedaPrecio;
import com.watts2crypto.watts2crypto_backend.service.CriptomonedaService;

@WebMvcTest(CriptomonedaController.class)
@AutoConfigureMockMvc(addFilters = false)
class CriptomonedaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CriptomonedaService service;

    @MockitoBean
    private RestTemplateBuilder restTemplateBuilder;

    @Test
    void returnsAllCryptocurrencies() throws Exception {
        when(service.findAllCriptomonedas()).thenReturn(List.of(new Criptomoneda(1L, "bitcoin", "BTC", "Bitcoin")));

        mockMvc.perform(get("/api/criptomonedas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].assetId").value("bitcoin"))
                .andExpect(jsonPath("$[0].simbolo").value("BTC"));
    }

    @Test
    void returnsAllNames() throws Exception {
        when(service.findAllNames()).thenReturn(List.of("Bitcoin", "Ethereum"));

        mockMvc.perform(get("/api/criptomonedas/nombres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Bitcoin"))
                .andExpect(jsonPath("$[1]").value("Ethereum"));
    }

    @Test
    void returnsAllSymbols() throws Exception {
        when(service.findAllSymbols()).thenReturn(List.of("BTC", "ETH"));

        mockMvc.perform(get("/api/criptomonedas/simbolos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("BTC"))
                .andExpect(jsonPath("$[1]").value("ETH"));
    }

    @Test
    void returnsCryptocurrencyByName() throws Exception {
        when(service.findCriptomonedaByName("Bitcoin"))
                .thenReturn(new Criptomoneda(1L, "bitcoin", "BTC", "Bitcoin"));

        mockMvc.perform(get("/api/criptomonedas/Bitcoin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Bitcoin"));
    }

    @Test
    void returnsCryptocurrencyByAssetId() throws Exception {
        when(service.findCriptomonedaPorAssetId("bitcoin"))
                .thenReturn(new Criptomoneda(1L, "bitcoin", "BTC", "Bitcoin"));

        mockMvc.perform(get("/api/criptomonedas/byAsset/bitcoin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.assetId").value("bitcoin"));
    }

    @Test
    void returnsPriceHistoryByDateRange() throws Exception {
        Criptomoneda bitcoin = new Criptomoneda(1L, "bitcoin", "BTC", "Bitcoin");
        when(service.findByDateRange("Bitcoin", LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 2)))
                .thenReturn(List.of(new CriptomonedaPrecio(BigDecimal.valueOf(100.50), LocalDate.of(2026, 5, 1), bitcoin)));

        mockMvc.perform(get("/api/criptomonedas/Bitcoin/history")
                .param("start", "2026-05-01")
                .param("end", "2026-05-02"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].precioEur").value(100.50))
                .andExpect(jsonPath("$[0].fecha").value("2026-05-01"));
    }

    @Test
    void returnsDirectPriceHistory() throws Exception {
        Criptomoneda bitcoin = new Criptomoneda(1L, "bitcoin", "BTC", "Bitcoin");
        when(service.findHistoricoDirectoPorSimbolo("BTC"))
                .thenReturn(List.of(new CriptomonedaPrecio(BigDecimal.valueOf(100.50), LocalDate.of(2026, 5, 1), bitcoin)));

        mockMvc.perform(get("/api/criptomonedas/BTC/direct/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].precioEur").value(100.50));
    }
}