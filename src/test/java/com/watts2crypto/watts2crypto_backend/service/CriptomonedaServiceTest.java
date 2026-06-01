package com.watts2crypto.watts2crypto_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import com.watts2crypto.watts2crypto_backend.models.Criptomoneda;
import com.watts2crypto.watts2crypto_backend.models.CriptomonedaPrecio;
import com.watts2crypto.watts2crypto_backend.repositories.CriptomonedaPrecioRepository;
import com.watts2crypto.watts2crypto_backend.repositories.CriptomonedaRepository;

@ExtendWith(MockitoExtension.class)
class CriptomonedaServiceTest {

    @Mock
    private CriptomonedaRepository repository;

    @Mock
    private CriptomonedaPrecioRepository precioRepository;

    @Mock
    private MonedaTradicionalService monedaTradicionalService;

    @Mock
    private org.springframework.web.client.RestTemplate restTemplate;

    @Mock
    private DirectRequestAvailabilityService directRequestAvailabilityService;

    @InjectMocks
    private CriptomonedaService service;

    @Test
    void findAllNamesReturnsRepositoryResults() {
        when(repository.findAllNames()).thenReturn(List.of("Bitcoin", "Ethereum"));

        assertEquals(List.of("Bitcoin", "Ethereum"), service.findAllNames());
    }

    @Test
    void findAllNamesThrowsWhenRepositoryReturnsEmpty() {
        when(repository.findAllNames()).thenReturn(List.of());

        assertThrows(ResponseStatusException.class, () -> service.findAllNames());
    }

    @Test
    void findAllSymbolsReturnsRepositoryResults() {
        when(repository.findAllSymbols()).thenReturn(List.of("BTC", "ETH"));

        assertEquals(List.of("BTC", "ETH"), service.findAllSymbols());
    }

    @Test
    void findAllSymbolsThrowsWhenRepositoryReturnsEmpty() {
        when(repository.findAllSymbols()).thenReturn(List.of());

        assertThrows(ResponseStatusException.class, () -> service.findAllSymbols());
    }

    @Test
    void findAllCriptomonedasThrowsWhenRepositoryReturnsEmpty() {
        when(repository.findAll()).thenReturn(List.of());

        assertThrows(ResponseStatusException.class, () -> service.findAllCriptomonedas());
    }

    @Test
    void findCriptomonedaByNameReturnsRepositoryResult() {
        Criptomoneda bitcoin = new Criptomoneda(1L, "bitcoin", "BTC", "Bitcoin");
        when(repository.findByNameIgnoreCase("Bitcoin")).thenReturn(Optional.of(bitcoin));

        assertEquals("bitcoin", service.findCriptomonedaByName("Bitcoin").getAssetId());
    }

    @Test
    void findCriptomonedaPorAssetIdReturnsRepositoryResultWhenPresent() {
        Criptomoneda bitcoin = new Criptomoneda(1L, "bitcoin", "BTC", "Bitcoin");
        when(repository.findByAssetIdIgnoreCase("bitcoin")).thenReturn(Optional.of(bitcoin));

        assertEquals("BTC", service.findCriptomonedaPorAssetId("bitcoin").getSimbolo());
    }

    @Test
    void findCriptomonedaPorAssetIdFallsBackToDirectApiWhenRepositoryMisses() {
        when(repository.findByAssetIdIgnoreCase("bitcoin")).thenReturn(Optional.empty());
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(ResponseEntity.ok("""
                        {"data":{"id":"bitcoin","symbol":"BTC","name":"Bitcoin","priceUsd":"50000.00"},"timestamp":1717027200000}
                        """));
        when(monedaTradicionalService.convertirCantidad(50000.0, "USD", "EUR")).thenReturn(45000.0);

        Criptomoneda result = service.findCriptomonedaPorAssetId("bitcoin");

        assertEquals("bitcoin", result.getAssetId());
        assertEquals(1, result.getHistoricoPrecios().size());
        assertEquals(BigDecimal.valueOf(45000.0).setScale(2), result.getHistoricoPrecios().get(0).getPrecioEur());
        assertFalse(result.getHistoricoPrecios().get(0).getFecha().isAfter(LocalDate.of(2024, 5, 30)));
    }

    @Test
    void findCriptomonedaPorAssetIdThrowsWhenDirectApiReturnsNoData() {
        when(repository.findByAssetIdIgnoreCase("bitcoin")).thenReturn(Optional.empty());
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(ResponseEntity.ok("{" +
                        "\"data\":null," +
                        "\"timestamp\":1717027200000" +
                        "}"));

        assertThrows(ResponseStatusException.class, () -> service.findCriptomonedaPorAssetId("bitcoin"));
    }

    @Test
    void findHistoricoDirectoPorSimboloSortsResults() {
        when(repository.findAssetIdBySymbol("BTC")).thenReturn("bitcoin");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(ResponseEntity.ok("""
                        {"data":{"id":"bitcoin","symbol":"BTC","name":"Bitcoin","priceUsd":"50000.00"},"timestamp":1717027200000}
                        """))
                .thenReturn(ResponseEntity.ok("""
                        {"data":[
                          {"priceUsd":"51000.00","date":"2024-05-31T00:00:00Z"},
                          {"priceUsd":"50000.00","time":1717027200000}
                        ]}
                        """));
        when(monedaTradicionalService.convertirCantidad(51000.0, "USD", "EUR")).thenReturn(45900.0);
        when(monedaTradicionalService.convertirCantidad(50000.0, "USD", "EUR")).thenReturn(45000.0);

        List<CriptomonedaPrecio> result = service.findHistoricoDirectoPorSimbolo("BTC");

        assertEquals(2, result.size());
        assertEquals(45000.0, result.get(0).getPrecioEur().doubleValue());
        assertEquals(45900.0, result.get(1).getPrecioEur().doubleValue());
    }

    @Test
    void findByDateRangeRejectsInvalidRanges() {
        when(precioRepository.findByNombreAndDateRange(eq("BTC"), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of());

        assertThrows(ResponseStatusException.class,
                () -> service.findByDateRange("BTC", LocalDate.of(2026, 5, 31), LocalDate.of(2026, 5, 30)));
    }

    @Test
    void findByDateRangeReturnsRepositoryResults() {
        Criptomoneda bitcoin = new Criptomoneda(1L, "bitcoin", "BTC", "Bitcoin");
        CriptomonedaPrecio precio = new CriptomonedaPrecio(BigDecimal.valueOf(45000.0), LocalDate.of(2026, 5, 30), bitcoin);
        when(precioRepository.findByNombreAndDateRange(eq("BTC"), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(precio));

        assertEquals(1, service.findByDateRange("BTC", LocalDate.of(2026, 5, 30), LocalDate.of(2026, 5, 31)).size());
    }

        @Test
        void findByDateRangeThrowsWhenRepositoryFails() {
        when(precioRepository.findByNombreAndDateRange(eq("BTC"), any(LocalDate.class), any(LocalDate.class)))
            .thenThrow(new RuntimeException("boom"));

        assertThrows(ResponseStatusException.class,
            () -> service.findByDateRange("BTC", LocalDate.of(2026, 5, 30), LocalDate.of(2026, 5, 31)));
        }

    @Test
    void findCriptomonedaByNameThrowsWhenMissing() {
        when(repository.findByNameIgnoreCase("Missing")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> service.findCriptomonedaByName("Missing"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void refreshCriptomonedasPersistsMetadataAndHistoricalPrices() {
    String metadataBody = "{" +
        "\"data\":[{" +
        "\"id\":\"bitcoin\"," +
        "\"symbol\":\"BTC\"," +
        "\"name\":\"Bitcoin\"" +
        "}]" +
        "}";
    String historyBody = "{" +
        "\"data\":[{" +
        "\"priceUsd\":\"50000.00\"," +
        "\"date\":\"2024-05-31T00:00:00Z\"" +
        "}]" +
        "}";

    when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
        .thenAnswer(invocation -> {
            String url = invocation.getArgument(0);
            if ("https://rest.coincap.io/v3/assets/".equals(url)) {
            return ResponseEntity.ok(metadataBody);
            }
            return ResponseEntity.ok(historyBody);
        });

    Criptomoneda bitcoin = new Criptomoneda(1L, "bitcoin", "BTC", "Bitcoin");
    when(repository.findByAssetIdIgnoreCase("bitcoin")).thenAnswer(invocation -> {
        String assetId = invocation.getArgument(0);
        return "bitcoin".equalsIgnoreCase(assetId) ? Optional.of(bitcoin) : Optional.empty();
    });
    when(monedaTradicionalService.convertirCantidad(50000.0, "USD", "EUR")).thenReturn(45000.0);

    service.refreshCriptomonedas();

    ArgumentCaptor<List<CriptomonedaPrecio>> captor = ArgumentCaptor.forClass(List.class);
    verify(precioRepository).saveAll(captor.capture());
    assertEquals(1, captor.getValue().size());
    assertEquals(BigDecimal.valueOf(45000.0).setScale(2), captor.getValue().get(0).getPrecioEur());
    }

    @Test
    void refreshCriptomonedasContinuesWhenHistoricalLoadFailsForOneAsset() {
        String metadataBody = "{" +
                "\"data\":[{" +
                "\"id\":\"bitcoin\"," +
                "\"symbol\":\"BTC\"," +
                "\"name\":\"Bitcoin\"" +
                "}]" +
                "}";
        String historyBody = "{" +
                "\"data\":[{" +
                "\"priceUsd\":\"50000.00\"," +
                "\"date\":\"2024-05-31T00:00:00Z\"" +
                "}]" +
                "}";

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenAnswer(invocation -> {
                    String url = invocation.getArgument(0);
                    if ("https://rest.coincap.io/v3/assets/".equals(url)) {
                        return ResponseEntity.ok(metadataBody);
                    }
                    return ResponseEntity.ok(historyBody);
                });

        Criptomoneda bitcoin = new Criptomoneda(1L, "bitcoin", "BTC", "Bitcoin");
        when(repository.findByAssetIdIgnoreCase("bitcoin")).thenReturn(Optional.of(bitcoin));
        doThrow(new RuntimeException("history boom")).when(precioRepository).saveAll(any());

        when(monedaTradicionalService.convertirCantidad(50000.0, "USD", "EUR")).thenReturn(45000.0);

        service.refreshCriptomonedas();

        verify(precioRepository).deleteAll();
    }
}