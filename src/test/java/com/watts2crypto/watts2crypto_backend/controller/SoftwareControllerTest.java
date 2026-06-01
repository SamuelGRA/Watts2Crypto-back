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

import com.watts2crypto.watts2crypto_backend.models.Software;
import com.watts2crypto.watts2crypto_backend.service.SoftwareService;

@WebMvcTest(SoftwareController.class)
@AutoConfigureMockMvc(addFilters = false)
class SoftwareControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SoftwareService service;

        @MockitoBean
        private RestTemplateBuilder restTemplateBuilder;

    @Test
    void returnsAllSoftware() throws Exception {
        when(service.findAll()).thenReturn(List.of(new Software("T-Rex", Set.of(Software.HardwareUsable.GPU),
                Set.of(Software.SistemaOperativo.WINDOWS), "trex")));

        mockMvc.perform(get("/api/software"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("T-Rex"))
                .andExpect(jsonPath("$[0].hashrateSlug").value("trex"));
    }

    @Test
    void returnsSoftwareByName() throws Exception {
        when(service.findByName("T-Rex")).thenReturn(
                new Software("T-Rex", Set.of(Software.HardwareUsable.GPU), Set.of(Software.SistemaOperativo.WINDOWS), "trex"));

        mockMvc.perform(get("/api/software/T-Rex"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("T-Rex"))
                .andExpect(jsonPath("$.hardwareUsable[0]").value("GPU"));
    }

    @Test
    void returnsAllSoftwareNames() throws Exception {
        when(service.findAllNames()).thenReturn(List.of("T-Rex", "Gminer"));

        mockMvc.perform(get("/api/software/nombres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("T-Rex"))
                .andExpect(jsonPath("$[1]").value("Gminer"));
    }

    @Test
    void returnsSoftwareByAlgorithm() throws Exception {
        when(service.findAvailableSoftwaresByAlgorithm("kawpow")).thenReturn(List.of("T-Rex"));

        mockMvc.perform(get("/api/software/byAlgoritmo/kawpow"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("T-Rex"));
    }
}