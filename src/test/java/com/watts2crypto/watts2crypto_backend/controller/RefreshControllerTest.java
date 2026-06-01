package com.watts2crypto.watts2crypto_backend.controller;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.watts2crypto.watts2crypto_backend.service.AsicService;
import com.watts2crypto.watts2crypto_backend.service.CpuService;
import com.watts2crypto.watts2crypto_backend.service.CriptomonedaService;
import com.watts2crypto.watts2crypto_backend.service.ElectricidadService;
import com.watts2crypto.watts2crypto_backend.service.GpuService;
import com.watts2crypto.watts2crypto_backend.service.MaintenanceStateService;
import com.watts2crypto.watts2crypto_backend.service.MetricasMinadoService;
import com.watts2crypto.watts2crypto_backend.service.MonedaTradicionalService;
import com.watts2crypto.watts2crypto_backend.service.PoolService;
import com.watts2crypto.watts2crypto_backend.service.SoftwareService;

@WebMvcTest(value = RefreshController.class, properties = "refresh.token=test-token")
@ActiveProfiles("prod")
@AutoConfigureMockMvc(addFilters = false)
class RefreshControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MonedaTradicionalService monedaTradicionalService;

    @MockitoBean
    private CriptomonedaService criptomonedaService;

    @MockitoBean
    private GpuService gpuService;

    @MockitoBean
    private AsicService asicService;

    @MockitoBean
    private CpuService cpuService;

    @MockitoBean
    private MetricasMinadoService metricaMinadoService;

    @MockitoBean
    private PoolService poolService;

    @MockitoBean
    private SoftwareService softwareService;

    @MockitoBean
    private ElectricidadService electricidadService;

    @MockitoBean
    private MaintenanceStateService maintenanceStateService;

    @MockitoBean
    private RestTemplateBuilder restTemplateBuilder;

    @Test
    void refreshFiatUsesConfiguredTokenAndMarksOperation() throws Exception {
        mockMvc.perform(post("/api/refresh/fiat").header("X-Refresh-Token", "test-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ok"))
                .andExpect(jsonPath("$.operation").value("refresh-fiat"));

        verify(maintenanceStateService).beginRefresh("refresh-fiat");
        verify(monedaTradicionalService).refreshMonedasTradicionales();
        verify(maintenanceStateService).endRefresh("refresh-fiat");
    }

    @Test
    void refreshSoftwareAcceptsBearerAuthorization() throws Exception {
        mockMvc.perform(post("/api/refresh/software").header("Authorization", "Bearer test-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("refresh-software"));

        verify(softwareService).refreshSoftware();
    }

    @Test
    void refreshWithWrongTokenIsUnauthorized() throws Exception {
        mockMvc.perform(post("/api/refresh/pools").header("X-Refresh-Token", "wrong-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void refreshCriptomonedasAndElectricidadAreRoutedToTheirServices() throws Exception {
        mockMvc.perform(post("/api/refresh/criptomonedas").header("X-Refresh-Token", "test-token"))
                .andExpect(status().isOk());
        mockMvc.perform(post("/api/refresh/electricidad").header("X-Refresh-Token", "test-token"))
                .andExpect(status().isOk());

        verify(criptomonedaService).refreshCriptomonedas();
        verify(electricidadService).refreshElectricidad();
    }

    @Test
    void refreshGpuAsicCpuAndMetricsAreRoutedToTheirServices() throws Exception {
        mockMvc.perform(post("/api/refresh/gpus").header("X-Refresh-Token", "test-token"))
                .andExpect(status().isOk());
        mockMvc.perform(post("/api/refresh/asics").header("X-Refresh-Token", "test-token"))
                .andExpect(status().isOk());
        mockMvc.perform(post("/api/refresh/cpus").header("X-Refresh-Token", "test-token"))
                .andExpect(status().isOk());
        mockMvc.perform(post("/api/refresh/metricas-minado").header("X-Refresh-Token", "test-token"))
                .andExpect(status().isOk());

        verify(gpuService).refreshGpus();
        verify(asicService).refreshAsics();
        verify(cpuService).refreshCpus();
        verify(metricaMinadoService).refreshMetricasMinado();
    }
}