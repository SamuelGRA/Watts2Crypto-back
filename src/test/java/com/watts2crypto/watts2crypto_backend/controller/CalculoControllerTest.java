package com.watts2crypto.watts2crypto_backend.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.watts2crypto.watts2crypto_backend.models.MetricasMinado;
import com.watts2crypto.watts2crypto_backend.models.DTOs.CalculoInputDto;
import com.watts2crypto.watts2crypto_backend.models.DTOs.CalculoOutputDto;
import com.watts2crypto.watts2crypto_backend.models.DTOs.MonedaAlgoritmosDto;
import com.watts2crypto.watts2crypto_backend.service.CalculoService;
import com.watts2crypto.watts2crypto_backend.service.MetricasMinadoService;

@WebMvcTest(CalculoController.class)
@AutoConfigureMockMvc(addFilters = false)
class CalculoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CalculoService service;

    @MockitoBean
    private MetricasMinadoService mmService;

    @MockitoBean
    private RestTemplateBuilder restTemplateBuilder;

    @Test
    void calculatesProfitability() throws Exception {
        when(service.calcularRentabilidad(any(CalculoInputDto.class)))
                .thenReturn(new CalculoOutputDto(10.0, 12.0, 300.0, 30.0));

        mockMvc.perform(post("/api/calculo/rentabilidad")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"hashrate\":100.0," +
                        "\"moneda\":\"BTC\"," +
                        "\"consumoW\":300.0," +
                        "\"precioKwh\":0.2," +
                        "\"comision\":1.0," +
                        "\"hardwareItems\":[{" +
                            "\"tipoHardware\":\"GPU\"," +
                            "\"nombreHardware\":\"RTX 3080\"," +
                            "\"cantidad\":2" +
                        "}]," +
                        "\"algoritmo\":\"kawpow\"," +
                        "\"pais\":\"ES\"," +
                        "\"pool\":\"Binance Pool\"," +
                        "\"software\":\"T-Rex\"," +
                        "\"costoInicialHardware\":1000.0" +
                        "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.beneficioDiario").value(10.0))
                .andExpect(jsonPath("$.roiDias").value(30.0));

        verify(service).calcularRentabilidad(any(CalculoInputDto.class));
    }

    @Test
    void returnsAvailableCoinsForCalculation() throws Exception {
        when(mmService.findAllNombresMonedasParaCalculo()).thenReturn(List.of("Bitcoin", "Ethereum"));

        mockMvc.perform(get("/api/calculo/monedasParaCalculo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Bitcoin"));
    }

    @Test
    void returnsCoinsWithAlgorithm() throws Exception {
        when(mmService.findAllMonedasParaCalculoConAlgoritmo())
                .thenReturn(List.of(new MonedaAlgoritmosDto("Bitcoin", java.util.Set.of("SHA-256"))));

        mockMvc.perform(get("/api/calculo/monedasParaCalculoConAlgoritmo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Bitcoin"))
                .andExpect(jsonPath("$[0].algoritmos[0]").value("SHA-256"));
    }

    @Test
    void returnsMetricsByCoin() throws Exception {
        when(mmService.findMetricasByNombre("Bitcoin")).thenReturn(
                new MetricasMinado(1, "Bitcoin", "SHA-256", 123L, 600.0, 6.25, 65000.0));

        mockMvc.perform(get("/api/calculo/metricas/Bitcoin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreMoneda").value("Bitcoin"))
                .andExpect(jsonPath("$.algoritmo").value("SHA-256"));
    }

    @Test
    void returnsElectricityCountries() throws Exception {
        when(service.findPaisesElectricidad()).thenReturn(List.of());

        mockMvc.perform(get("/api/calculo/paisesElectricidad"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }
}