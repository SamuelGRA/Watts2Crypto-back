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

import com.watts2crypto.watts2crypto_backend.models.Asic;
import com.watts2crypto.watts2crypto_backend.models.RendimientoAlgoritmo;
import com.watts2crypto.watts2crypto_backend.service.AsicService;

@WebMvcTest(AsicController.class)
@AutoConfigureMockMvc(addFilters = false)
class AsicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AsicService service;

    @MockitoBean
    private RestTemplateBuilder restTemplateBuilder;

    @Test
    void returnsAllAsics() throws Exception {
        when(service.findAllAsics()).thenReturn(List.of(new Asic("Antminer S19", 3250, 110.0,
                Map.of("sha256", new RendimientoAlgoritmo(110_000_000_000.0, 3250)))));

        mockMvc.perform(get("/api/asics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Antminer S19"))
                .andExpect(jsonPath("$[0].algorithms.sha256.hashrate").value(110000000000.0));
    }

    @Test
    void returnsAsicByName() throws Exception {
        when(service.findAsicByName("Antminer S19")).thenReturn(new Asic("Antminer S19", 3250, 110.0,
                Map.of("sha256", new RendimientoAlgoritmo(110_000_000_000.0, 3250))));

        mockMvc.perform(get("/api/asics/Antminer S19"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Antminer S19"));
    }

    @Test
    void returnsAllAsicNames() throws Exception {
        when(service.findAllAsicNames()).thenReturn(List.of("Antminer S19", "Whatsminer M50"));

        mockMvc.perform(get("/api/asics/nombres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Antminer S19"))
                .andExpect(jsonPath("$[1]").value("Whatsminer M50"));
    }

    @Test
    void returnsAsicPerformanceForAlgorithm() throws Exception {
        when(service.findHashrateYConsumoPorNombreYAlgoritmo("Antminer S19", "sha256"))
                .thenReturn(new RendimientoAlgoritmo(110_000_000_000.0, 3250));

        mockMvc.perform(get("/api/asics/Antminer S19/sha256"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.consumo").value(3250));
    }

    @Test
    void returnsAsicsByAlgorithm() throws Exception {
        when(service.findNamesByAlgorithm("sha256")).thenReturn(List.of("Antminer S19"));

        mockMvc.perform(get("/api/asics/byAlgoritmo/sha256"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Antminer S19"));
    }
}