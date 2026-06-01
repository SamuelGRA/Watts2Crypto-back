package com.watts2crypto.watts2crypto_backend.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.web.servlet.MockMvc;

import com.watts2crypto.watts2crypto_backend.models.Pool;
import com.watts2crypto.watts2crypto_backend.service.PoolService;

@WebMvcTest(PoolController.class)
@AutoConfigureMockMvc(addFilters = false)
class PoolControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PoolService service;

    @MockitoBean
    private RestTemplateBuilder restTemplateBuilder;

    @Test
    void returnsAllPools() throws Exception {
        when(service.findAll()).thenReturn(List.of(new Pool("Binance Pool", Set.of(Pool.EsquemaDePago.FPPS),
                List.of("EU"), "binance-pool")));

        mockMvc.perform(get("/api/pools"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Binance Pool"))
                .andExpect(jsonPath("$[0].hashrateSlug").value("binance-pool"));
    }

    @Test
    void returnsPoolByName() throws Exception {
        when(service.findByName("Binance Pool")).thenReturn(new Pool("Binance Pool",
                Set.of(Pool.EsquemaDePago.FPPS), List.of("EU"), "binance-pool"));

        mockMvc.perform(get("/api/pools/Binance Pool"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Binance Pool"));
    }

    @Test
    void returnsAllPoolNames() throws Exception {
        when(service.findAllNames()).thenReturn(List.of("Binance Pool", "F2Pool"));

        mockMvc.perform(get("/api/pools/nombres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Binance Pool"))
                .andExpect(jsonPath("$[1]").value("F2Pool"));
    }

    @Test
    void returnsPoolsByAlgorithm() throws Exception {
        when(service.findAvailablePoolsByAlgorithm("sha256")).thenReturn(List.of("Binance Pool"));

        mockMvc.perform(get("/api/pools/byAlgoritmo/sha256"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Binance Pool"));
    }
}