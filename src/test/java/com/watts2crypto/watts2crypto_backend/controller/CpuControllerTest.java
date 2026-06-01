package com.watts2crypto.watts2crypto_backend.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.web.servlet.MockMvc;

import com.watts2crypto.watts2crypto_backend.models.Cpu;
import com.watts2crypto.watts2crypto_backend.service.CpuService;

@WebMvcTest(CpuController.class)
@AutoConfigureMockMvc(addFilters = false)
class CpuControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CpuService service;

    @MockitoBean
    private RestTemplateBuilder restTemplateBuilder;

    @Test
    void returnsAllCpus() throws Exception {
        when(service.findAllCpus()).thenReturn(List.of(new Cpu("Ryzen 9", 65, 100)));

        mockMvc.perform(get("/api/cpus"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Ryzen 9"))
                .andExpect(jsonPath("$[0].hashrate").value(100));
    }

    @Test
    void returnsCpuByName() throws Exception {
        when(service.findCpuByName("Ryzen 9")).thenReturn(new Cpu("Ryzen 9", 65, 100));

        mockMvc.perform(get("/api/cpus/Ryzen 9"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Ryzen 9"));
    }

    @Test
    void returnsAllCpuNames() throws Exception {
        when(service.findAllCpuNames()).thenReturn(List.of("Ryzen 9", "Core i9"));

        mockMvc.perform(get("/api/cpus/nombres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Ryzen 9"))
                .andExpect(jsonPath("$[1]").value("Core i9"));
    }

    @Test
    void returnsCpusByAlgorithm() throws Exception {
        when(service.findNamesByAlgorithm("RandomX")).thenReturn(List.of("Ryzen 9"));

        mockMvc.perform(get("/api/cpus/byAlgoritmo/RandomX"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Ryzen 9"));
    }
}