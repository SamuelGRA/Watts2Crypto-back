package com.watts2crypto.watts2crypto_backend.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.web.servlet.MockMvc;

import com.watts2crypto.watts2crypto_backend.models.Gpu;
import com.watts2crypto.watts2crypto_backend.models.RendimientoAlgoritmo;
import com.watts2crypto.watts2crypto_backend.service.GpuService;

@WebMvcTest(GpuController.class)
@AutoConfigureMockMvc(addFilters = false)
class GpuControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GpuService service;

    @MockitoBean
    private RestTemplateBuilder restTemplateBuilder;

    @Test
    void returnsAllGpus() throws Exception {
        when(service.findAllGpus()).thenReturn(List.of(new Gpu("RTX 3080", 320, 1000.0,
                Map.of("kawpow", new RendimientoAlgoritmo(123.0, 150)))));

        mockMvc.perform(get("/api/gpus"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("RTX 3080"))
                .andExpect(jsonPath("$[0].algorithms.kawpow.hashrate").value(123.0));
    }

    @Test
    void returnsGpuByName() throws Exception {
        when(service.findGpuByName("RTX 3080")).thenReturn(new Gpu("RTX 3080", 320, 1000.0,
                Map.of("kawpow", new RendimientoAlgoritmo(123.0, 150))));

        mockMvc.perform(get("/api/gpus/RTX 3080"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("RTX 3080"));
    }

    @Test
    void returnsAllGpuNames() throws Exception {
        when(service.findAllGpuNames()).thenReturn(List.of("RTX 3080", "RX 6800"));

        mockMvc.perform(get("/api/gpus/nombres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("RTX 3080"))
                .andExpect(jsonPath("$[1]").value("RX 6800"));
    }

    @Test
    void returnsGpuPerformanceForAlgorithm() throws Exception {
        when(service.findHashrateYConsumoPorNombreYAlgoritmo("RTX 3080", "kawpow"))
                .thenReturn(new RendimientoAlgoritmo(123.0, 150));

        mockMvc.perform(get("/api/gpus/RTX 3080/kawpow"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hashrate").value(123.0))
                .andExpect(jsonPath("$.consumo").value(150));
    }

    @Test
    void returnsGpusByAlgorithm() throws Exception {
        when(service.findNamesByAlgorithm("kawpow")).thenReturn(List.of("RTX 3080"));

        mockMvc.perform(get("/api/gpus/byAlgoritmo/kawpow"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("RTX 3080"));
    }
}