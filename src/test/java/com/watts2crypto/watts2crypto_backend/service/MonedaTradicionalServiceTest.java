package com.watts2crypto.watts2crypto_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.watts2crypto.watts2crypto_backend.models.MonedaTradicional;
import com.watts2crypto.watts2crypto_backend.repositories.MonedaTradicionalRepository;
import com.watts2crypto.watts2crypto_backend.repositories.SimbolosISOMonedasRepository;

@ExtendWith(MockitoExtension.class)
class MonedaTradicionalServiceTest {

        @Mock
        private MonedaTradicionalRepository repository;

        @Mock
        private SimbolosISOMonedasRepository simbolosRepository;

        @Mock
        private RestTemplate restTemplate;

        @InjectMocks
        private MonedaTradicionalService service;

        @Test
        void refreshMonedasTradicionalesParsesApiResponseAndPersistsIt() {
                String body = """
                                [{"quote":"USD","rate":1.08,"date":"2026-05-30"},{"quote":"JPY","rate":160.0,"date":"2026-05-30"}]
                                """;
                when(restTemplate.exchange(anyString(), org.mockito.ArgumentMatchers.eq(HttpMethod.GET), any(),
                                org.mockito.ArgumentMatchers.eq(String.class)))
                                .thenReturn(ResponseEntity.ok(body));

                service.refreshMonedasTradicionales();

                verify(repository).deleteAll();
                verify(repository).saveAll(anyList());
        }

        @Test
        void refreshSymbolsParsesObjectResponseAndPersistsSymbols() {
                String body = """
                                {"USD":"US Dollar","EUR":"Euro","JPY":"Yen"}
                                """;
                when(restTemplate.exchange(anyString(), org.mockito.ArgumentMatchers.eq(HttpMethod.GET), any(),
                                org.mockito.ArgumentMatchers.eq(String.class)))
                                .thenReturn(ResponseEntity.ok(body));

                service.refreshSymbols();

                verify(simbolosRepository).deleteAll();
                verify(simbolosRepository).saveAll(anyList());
        }

        @Test
        void findTasaCambioDirectaReturnsParsedValue() {
                String body = """
                                [{"base":"USD","quote":"EUR","rate":0.92,"date":"2026-05-30"}]
                                """;
                when(restTemplate.exchange(anyString(), org.mockito.ArgumentMatchers.eq(HttpMethod.GET), any(),
                                org.mockito.ArgumentMatchers.eq(String.class)))
                                .thenReturn(ResponseEntity.ok(body));

                MonedaTradicional result = service.findTasaCambioDirecta("USD", "EUR", LocalDate.of(2026, 5, 30));

                assertEquals("USD", result.getMonedaBase());
                assertEquals("EUR", result.getMonedaObjetivo());
                assertEquals(0.92, result.getTasaCambio());
                assertEquals(LocalDate.of(2026, 5, 30), result.getFecha());
        }

        @Test
        @SuppressWarnings("unchecked")
        void findTasaCambioActualUsesRepositoryWhenAvailable() {
                LocalDate today = LocalDate.now(ZoneId.of("Europe/Madrid"));
                MonedaTradicional moneda = new MonedaTradicional("USD", "EUR", 0.9, today);
                when(repository.findByExactDate("USD", "EUR", today)).thenReturn(Optional.of(moneda));

                MonedaTradicional result = service.findTasaCambioActualPorBaseYObjetivo("USD", "EUR");

                assertEquals(0.9, result.getTasaCambio());
                verify(restTemplate, never()).exchange(anyString(), any(), any(), any(Class.class));
        }

        @Test
        void findTasaCambioActualFallsBackToApiWhenRepositoryMisses() {
                LocalDate today = LocalDate.now(ZoneId.of("Europe/Madrid"));
                String body = """
                                [{"base":"USD","quote":"EUR","rate":0.93,"date":"2026-05-30"}]
                                """;
                when(repository.findByExactDate("USD", "EUR", today)).thenReturn(Optional.empty());
                when(restTemplate.exchange(anyString(), org.mockito.ArgumentMatchers.eq(HttpMethod.GET), any(),
                                org.mockito.ArgumentMatchers.eq(String.class)))
                                .thenReturn(ResponseEntity.ok(body));

                MonedaTradicional result = service.findTasaCambioActualPorBaseYObjetivo("USD", "EUR");

                assertEquals(0.93, result.getTasaCambio());
        }

        @Test
        void findTasaCambioActualWrapsRepositoryFailures() {
                LocalDate today = LocalDate.now(ZoneId.of("Europe/Madrid"));
                when(repository.findByExactDate("USD", "EUR", today)).thenThrow(new RuntimeException("boom"));

                assertThrows(ResponseStatusException.class,
                                () -> service.findTasaCambioActualPorBaseYObjetivo("USD", "EUR"));
        }

        @Test
        void convertirCantidadUsesRepositoryRatesForCrossCurrencyConversions() {
                LocalDate today = LocalDate.now(ZoneId.of("Europe/Madrid"));
                when(repository.findByExactDate("USD", "EUR", today))
                                .thenReturn(Optional.of(new MonedaTradicional("USD", "EUR", 0.9, today)));
                when(repository.findByExactDate("EUR", "USD", today))
                                .thenReturn(Optional.of(new MonedaTradicional("EUR", "USD", 1.1, today)));

                assertEquals(10.0, service.convertirCantidad(10.0, "USD", "USD"));
                assertEquals(9.0, service.convertirCantidad(10.0, "USD", "EUR"));
                assertEquals(11.0, service.convertirCantidad(10.0, "EUR", "USD"));
        }

        @Test
        void findHistoricoTasaCambioDirectaCompletesDailySeries() {
                String body = """
                                [
                                  {"base":"USD","quote":"EUR","rate":0.91,"date":"2026-05-10"},
                                  {"base":"USD","quote":"EUR","rate":0.95,"date":"2026-05-12"}
                                ]
                                """;
                when(restTemplate.exchange(anyString(), org.mockito.ArgumentMatchers.eq(HttpMethod.GET), any(),
                                org.mockito.ArgumentMatchers.eq(String.class)))
                                .thenReturn(ResponseEntity.ok(body));

                List<MonedaTradicional> result = service.findHistoricoTasaCambioDirecta(
                                "USD",
                                "EUR",
                                LocalDate.of(2026, 5, 12),
                                LocalDate.of(2026, 5, 10));

                assertEquals(3, result.size());
                assertEquals(LocalDate.of(2026, 5, 10), result.get(0).getFecha());
                assertEquals(0.91, result.get(0).getTasaCambio());
                assertEquals(LocalDate.of(2026, 5, 11), result.get(1).getFecha());
                assertEquals(0.91, result.get(1).getTasaCambio());
                assertEquals(LocalDate.of(2026, 5, 12), result.get(2).getFecha());
                assertEquals(0.95, result.get(2).getTasaCambio());
        }

        @Test
        @SuppressWarnings("unchecked")
        void findAllSymbolsReturnsRepositoryValuesWithoutApiCall() {
                when(simbolosRepository.findAllSymbols()).thenReturn(List.of("USD", "EUR"));

                assertEquals(List.of("USD", "EUR"), service.findAllSymbols());
                verify(restTemplate, never()).exchange(anyString(), any(), any(), any(Class.class));
        }

        @Test
        void findAllSymbolsFallsBackToApiAndPersistsResult() {
                String body = """
                                {"USD":"US Dollar","EUR":"Euro"}
                                """;
                when(simbolosRepository.findAllSymbols()).thenReturn(List.of());
                when(restTemplate.exchange(anyString(), org.mockito.ArgumentMatchers.eq(HttpMethod.GET), any(),
                                org.mockito.ArgumentMatchers.eq(String.class)))
                                .thenReturn(ResponseEntity.ok(body));

                List<String> result = service.findAllSymbols();

                assertEquals(List.of("USD", "EUR"), result);
                verify(simbolosRepository).saveAll(anyList());
        }

        @Test
        void findAllSymbolsParsesArrayResponseWithIsoCodes() {
                String body = """
                                [{"iso_code":"USD"},{"iso_code":"EUR"},{"iso_code":"JPY"}]
                                """;
                when(simbolosRepository.findAllSymbols()).thenReturn(List.of());
                when(restTemplate.exchange(anyString(), org.mockito.ArgumentMatchers.eq(HttpMethod.GET), any(),
                                org.mockito.ArgumentMatchers.eq(String.class)))
                                .thenReturn(ResponseEntity.ok(body));

                List<String> result = service.findAllSymbols();

                assertEquals(List.of("USD", "EUR", "JPY"), result);
                verify(simbolosRepository).saveAll(anyList());
        }

        @Test
        void findAllSymbolsThrowsWhenApiReturnsNoSymbols() {
                when(simbolosRepository.findAllSymbols()).thenReturn(List.of());
                when(restTemplate.exchange(anyString(), org.mockito.ArgumentMatchers.eq(HttpMethod.GET), any(),
                                org.mockito.ArgumentMatchers.eq(String.class)))
                                .thenReturn(ResponseEntity.ok("{}"));

                assertThrows(ResponseStatusException.class, () -> service.findAllSymbols());
        }

        @Test
        void findAllMonedasTradicionalesReturnsRepositoryResults() {
                when(repository.findAll()).thenReturn(
                                List.of(new MonedaTradicional("EUR", "USD", 1.08, LocalDate.of(2026, 5, 30))));

                assertEquals(1, service.findAllMonedasTradicionales().size());
        }

        @Test
        void findAllMonedasTradicionalesThrowsWhenRepositoryFails() {
                when(repository.findAll()).thenThrow(new RuntimeException("boom"));

                assertThrows(ResponseStatusException.class, () -> service.findAllMonedasTradicionales());
        }

        @Test
        void findHistoricoPorBaseObjetivoYRangoFechasUsesRepositoryWhenAvailable() {
                List<MonedaTradicional> stored = List.of(
                                new MonedaTradicional("USD", "EUR", 0.9, LocalDate.of(2026, 5, 10)),
                                new MonedaTradicional("USD", "EUR", 0.91, LocalDate.of(2026, 5, 11)));
                when(repository.findByDateRange("USD", "EUR", LocalDate.of(2026, 5, 10), LocalDate.of(2026, 5, 11)))
                                .thenReturn(stored);

                assertEquals(stored, service.findHistoricoPorBaseObjetivoYRangoFechas(
                                "USD",
                                "EUR",
                                LocalDate.of(2026, 5, 10),
                                LocalDate.of(2026, 5, 11)));
        }

        @Test
        void findHistoricoPorBaseObjetivoYRangoFechasThrowsWhenApiReturnsBlankBody() {
                when(repository.findByDateRange("USD", "EUR", LocalDate.of(2026, 5, 10), LocalDate.of(2026, 5, 11)))
                                .thenReturn(List.of());
                when(restTemplate.exchange(anyString(), org.mockito.ArgumentMatchers.eq(HttpMethod.GET), any(),
                                org.mockito.ArgumentMatchers.eq(String.class)))
                                .thenReturn(ResponseEntity.ok("   "));

                assertThrows(ResponseStatusException.class,
                                () -> service.findHistoricoPorBaseObjetivoYRangoFechas(
                                                "USD",
                                                "EUR",
                                                LocalDate.of(2026, 5, 10),
                                                LocalDate.of(2026, 5, 11)));
        }

        @Test
        void findHistoricoPorBaseObjetivoYRangoFechasFallsBackWhenRepositoryIsEmpty() {
                String body = """
                                [{"base":"USD","quote":"EUR","rate":0.91,"date":"2026-05-10"}]
                                """;
                when(repository.findByDateRange("USD", "EUR", LocalDate.of(2026, 5, 10), LocalDate.of(2026, 5, 11)))
                                .thenReturn(List.of());
                when(restTemplate.exchange(anyString(), org.mockito.ArgumentMatchers.eq(HttpMethod.GET), any(),
                                org.mockito.ArgumentMatchers.eq(String.class)))
                                .thenReturn(ResponseEntity.ok(body));

                List<MonedaTradicional> result = service.findHistoricoPorBaseObjetivoYRangoFechas(
                                "USD",
                                "EUR",
                                LocalDate.of(2026, 5, 10),
                                LocalDate.of(2026, 5, 11));

                assertEquals(2, result.size());
                verify(restTemplate).exchange(anyString(), org.mockito.ArgumentMatchers.eq(HttpMethod.GET), any(),
                                org.mockito.ArgumentMatchers.eq(String.class));
        }

        @Test
        void findTasaCambioDirectaThrowsNotFoundWhenApiReturnsEmptyArray() {
                when(restTemplate.exchange(anyString(), org.mockito.ArgumentMatchers.eq(HttpMethod.GET), any(),
                                org.mockito.ArgumentMatchers.eq(String.class)))
                                .thenReturn(ResponseEntity.ok("[]"));

                assertThrows(ResponseStatusException.class,
                                () -> service.findTasaCambioDirecta("USD", "EUR", LocalDate.of(2026, 5, 30)));
        }
}