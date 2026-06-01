package com.watts2crypto.watts2crypto_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.test.util.ReflectionTestUtils;

import com.watts2crypto.watts2crypto_backend.models.Electricidad;
import com.watts2crypto.watts2crypto_backend.repositories.ElectricidadRepository;

@ExtendWith(MockitoExtension.class)
class ElectricidadServiceTest {

    @Mock
    private ElectricidadRepository repository;

    @Mock
    private org.springframework.web.client.RestTemplate restTemplate;

    @InjectMocks
    private ElectricidadService service;

    @Test
    void findElectricidadDirectaPorZonaParsesAndSortsResults() {
        long first = Instant.parse("2026-05-30T00:00:00Z").getEpochSecond();
        long second = Instant.parse("2026-05-31T00:00:00Z").getEpochSecond();
        String body = """
                {
                  "unix": [%d, %d],
                  "price": [100.0, 200.0]
                }
                """.formatted(first, second);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(org.springframework.http.HttpEntity.class),
                eq(String.class))).thenReturn(ResponseEntity.ok(body));

        List<Electricidad> result = service.findElectricidadDirectaPorZona("es");

        assertEquals(2, result.size());
        assertEquals("ES", result.get(0).getZona());
        assertEquals(100.0, result.get(0).getPrecioMwh());
        assertEquals(LocalDate.of(2026, 5, 30), result.get(0).getFecha().toLocalDate());
        assertEquals(LocalDate.of(2026, 5, 31), result.get(1).getFecha().toLocalDate());
    }

    @Test
    void findElectricidadDirectaPorZonaHandlesMillisecondTimestampsAndSkipsNegativePrices() {
        long first = Instant.parse("2026-05-30T00:00:00Z").toEpochMilli();
        long second = Instant.parse("2026-05-30T01:00:00Z").toEpochMilli();
        String body = """
                {
                  "serie1": [%d, %d],
                  "serie2": [100.0, -50.0]
                }
                """.formatted(first, second);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(org.springframework.http.HttpEntity.class),
                eq(String.class))).thenReturn(ResponseEntity.ok(body));

        List<Electricidad> result = service.findElectricidadDirectaPorZona("ES");

        assertEquals(1, result.size());
        assertEquals("ES", result.get(0).getZona());
        assertEquals(100.0, result.get(0).getPrecioMwh());
        assertEquals(LocalDate.of(2026, 5, 30), result.get(0).getFecha().toLocalDate());
    }

    @Test
    void findElectricidadDirectaPorZonaReturnsEmptyListWhenDailyAverageIsNotPositive() {
        long first = Instant.parse("2026-05-30T00:00:00Z").getEpochSecond();
        String body = """
                {
                  "unix": [%d],
                  "price": [0.0]
                }
                """.formatted(first);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(org.springframework.http.HttpEntity.class),
                eq(String.class))).thenReturn(ResponseEntity.ok(body));

        List<Electricidad> result = service.findElectricidadDirectaPorZona("ES");

        assertTrue(result.isEmpty());
    }

    @Test
    void llamarApiEnergyChartsRetriesOnceWhenApiReturnsTooManyRequests() {
        String body = """
                {
                  "unix": [1717027200],
                  "price": [123.45]
                }
                """;
        HttpClientErrorException tooManyRequests = HttpClientErrorException.create(
                HttpStatus.TOO_MANY_REQUESTS,
                "Too Many Requests",
                org.springframework.http.HttpHeaders.EMPTY,
                new byte[0],
                null);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(org.springframework.http.HttpEntity.class),
                eq(String.class)))
                .thenThrow(tooManyRequests)
                .thenReturn(ResponseEntity.ok(body));

        String result = ReflectionTestUtils.invokeMethod(service, "llamarApiEnergyCharts", "ES");

        assertEquals(body, result);
    }

    @Test
    void llamarApiEnergyChartsRetriesWhenExceptionContainsGoaway() {
        String body = """
                {
                  "unix": [1717027200],
                  "price": [123.45]
                }
                """;

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(org.springframework.http.HttpEntity.class),
                eq(String.class)))
                .thenThrow(new RuntimeException("connection reset by peer: goaway"))
                .thenReturn(ResponseEntity.ok(body));

        String result = ReflectionTestUtils.invokeMethod(service, "llamarApiEnergyCharts", "ES");

        assertEquals(body, result);
    }

    @Test
    void findElectricidadDirectaPorZonaRejectsUnsupportedZone() {
        assertThrows(ResponseStatusException.class, () -> service.findElectricidadDirectaPorZona("XX"));
    }

    @Test
    void findAllReturnsRepositoryResults() {
        when(repository.findAll()).thenReturn(List.of(new Electricidad("ES", LocalDateTime.now(), 1.0)));

        assertEquals(1, service.findAll().size());
    }

    @Test
    void findAllThrowsWhenRepositoryIsEmpty() {
        when(repository.findAll()).thenReturn(List.of());

        assertThrows(ResponseStatusException.class, service::findAll);
    }

    @Test
    void findByDateRangeAndZoneReturnsRepositoryResults() {
        Electricidad electricidad = new Electricidad("ES", LocalDateTime.now(), 1.0);
        when(repository.findByDateRange(eq("ES"), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(electricidad));

        assertEquals(1, service.findByDateRangeAndZone("ES", LocalDateTime.now().minusDays(1), LocalDateTime.now()).size());
    }

    @Test
    void findPriceByExactDateAndZoneReturnsPrice() {
        LocalDateTime date = LocalDateTime.of(2026, 5, 30, 0, 0);
        when(repository.findPriceBydateAndZone("ES", date)).thenReturn(Optional.of(1.23));

        assertEquals(1.23, service.findPriceByExactDateAndZone("ES", date));
    }

    @Test
    void findPriceByExactDateAndZoneThrowsWhenPriceIsMissing() {
        LocalDateTime date = LocalDateTime.of(2026, 5, 30, 0, 0);
        when(repository.findPriceBydateAndZone("ES", date)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> service.findPriceByExactDateAndZone("ES", date));
    }

    @Test
    void findLatestPriceInZoneReturnsLatestEntry() {
        Electricidad electricidad = new Electricidad("ES", LocalDateTime.of(2026, 5, 30, 0, 0), 1.23);
        when(repository.findFirstByZonaOrderByFechaDesc("ES")).thenReturn(Optional.of(electricidad));

        assertEquals(1.23, service.findLatestPriceInZone("ES").getPrecioMwh());
    }

    @Test
    void findLatestPriceInZoneThrowsWhenZoneHasNoPrices() {
        when(repository.findFirstByZonaOrderByFechaDesc("ES")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> service.findLatestPriceInZone("ES"));
    }

    @Test
    void findAllZonesReturnsRepositoryResults() {
        when(repository.findAllZoneNames()).thenReturn(List.of("ES", "FR"));

        assertEquals(List.of("ES", "FR"), service.findAllZones());
    }

    @Test
    void findAllZonesThrowsWhenRepositoryIsEmpty() {
        when(repository.findAllZoneNames()).thenReturn(List.of());

        assertThrows(ResponseStatusException.class, service::findAllZones);
    }

    @Test
    void findZonesForDirectSearchRemovesPersistedZones() {
        when(repository.findAllZoneNames()).thenReturn(List.of("ES", "FR"));

        List<String> result = service.findZonesForDirectSearch();

        assertFalse(result.contains("ES"));
        assertFalse(result.contains("FR"));
    }

}