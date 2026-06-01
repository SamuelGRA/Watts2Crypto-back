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

import com.watts2crypto.watts2crypto_backend.service.DirectRequestAvailabilityService;
import com.watts2crypto.watts2crypto_backend.service.MaintenanceStateService;

@WebMvcTest(MaintenanceController.class)
@AutoConfigureMockMvc(addFilters = false)
class MaintenanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MaintenanceStateService maintenanceStateService;

    @MockitoBean
    private DirectRequestAvailabilityService directRequestAvailabilityService;

    @MockitoBean
    private RestTemplateBuilder restTemplateBuilder;

    @Test
    void returnsMaintenanceStatusWhenRefreshing() throws Exception {
        when(maintenanceStateService.isRefreshing()).thenReturn(true);
        when(maintenanceStateService.getActiveRefreshes()).thenReturn(List.of("refresh-software"));
        when(directRequestAvailabilityService.isDirectCryptoAvailable()).thenReturn(false);

        mockMvc.perform(get("/api/maintenance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.maintenanceMode").value(true))
                .andExpect(jsonPath("$.message").value("Estamos actualizando nuestros datos, intente acceder más tarde"))
                .andExpect(jsonPath("$.activeRefreshes[0]").value("refresh-software"))
                .andExpect(jsonPath("$.directCryptoAvailable").value(false));
    }

    @Test
    void returnsNormalMaintenanceStatusWhenIdle() throws Exception {
        when(maintenanceStateService.isRefreshing()).thenReturn(false);
        when(maintenanceStateService.getActiveRefreshes()).thenReturn(List.of());
        when(directRequestAvailabilityService.isDirectCryptoAvailable()).thenReturn(true);

        mockMvc.perform(get("/api/maintenance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.maintenanceMode").value(false))
                .andExpect(jsonPath("$.message").doesNotExist())
                .andExpect(jsonPath("$.activeRefreshes").isArray())
                .andExpect(jsonPath("$.activeRefreshes").isEmpty())
                .andExpect(jsonPath("$.directCryptoAvailable").value(true));
    }
}