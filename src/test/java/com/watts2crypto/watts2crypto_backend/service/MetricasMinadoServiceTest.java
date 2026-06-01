package com.watts2crypto.watts2crypto_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.ArgumentCaptor;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.watts2crypto.watts2crypto_backend.models.Criptomoneda;
import com.watts2crypto.watts2crypto_backend.models.MetricasMinado;
import com.watts2crypto.watts2crypto_backend.models.DTOs.MonedaAlgoritmosDto;
import com.watts2crypto.watts2crypto_backend.repositories.CriptomonedaRepository;
import com.watts2crypto.watts2crypto_backend.repositories.MetricasMinadoRepository;

@ExtendWith(MockitoExtension.class)
class MetricasMinadoServiceTest {

        @Mock
        private MetricasMinadoRepository repository;

        @Mock
        private CriptomonedaRepository criptomonedaRepository;

        @Mock
        private org.springframework.web.client.RestTemplate restTemplate;

        @InjectMocks
        private MetricasMinadoService service;

        @Test
        @SuppressWarnings("unchecked")
        void refreshMetricasMinadoBuildsAndSavesParsedMetrics() {
                MetricasMinadoService localService = new MetricasMinadoService(repository, criptomonedaRepository,
                                restTemplate,
                                "test-key");

                String body = "["
                                + "{\"id\":1,\"name\":\"Dogecoin\",\"algorithm\":\"Scrypt\",\"nethash\":1000,"
                                + "\"block_time\":\"60\",\"block_reward24\":\"2.5\","
                                + "\"exchanges\":[{\"price\":0.5}]}"
                                + "]";

                when(restTemplate.exchange(
                                org.mockito.ArgumentMatchers.eq("https://whattomine.com/api/v1/coins"),
                                org.mockito.ArgumentMatchers.eq(HttpMethod.GET),
                                org.mockito.ArgumentMatchers.any(HttpEntity.class),
                                org.mockito.ArgumentMatchers.eq(String.class)))
                                .thenReturn(ResponseEntity.ok(body));

                Criptomoneda bitcoin = new Criptomoneda();
                bitcoin.setAssetId("bitcoin");
                when(criptomonedaRepository.findLatestPriceByAssetId("bitcoin"))
                                .thenReturn(Optional.of(BigDecimal.valueOf(50000)));

                Criptomoneda dogecoin = new Criptomoneda();
                dogecoin.setAssetId("dogecoin");
                when(criptomonedaRepository.findByNameIgnoreCase("Dogecoin")).thenReturn(Optional.of(dogecoin));
                when(criptomonedaRepository.findLatestPriceByAssetId("dogecoin"))
                                .thenReturn(Optional.of(BigDecimal.valueOf(123.45)));
                doNothing().when(repository).deleteAll();

                localService.refreshMetricasMinado();

                ArgumentCaptor<List<MetricasMinado>> captor = ArgumentCaptor.forClass(List.class);
                verify(repository).deleteAll();
                verify(repository).saveAll(captor.capture());

                List<MetricasMinado> saved = captor.getValue();
                assertEquals(1, saved.size());
                assertEquals("Dogecoin", saved.get(0).getNombreMoneda());
                assertEquals("Scrypt", saved.get(0).getAlgoritmo());
                assertEquals(123.45, saved.get(0).getPrecioActualEur());
        }

        @Test
        @SuppressWarnings("unchecked")
        void refreshMetricasMinadoSkipsCoinWithMissingApiFieldsAndExchangePrice() {
                MetricasMinadoService localService = new MetricasMinadoService(repository, criptomonedaRepository,
                                restTemplate,
                                "test-key");

                String body = "["
                                + "{"
                                + "\"block_time\":\"60\","
                                + "\"block_reward24\":\"0\","
                                + "\"nethash\":0,"
                                + "\"exchanges\":[{}]"
                                + "}"
                                + "]";

                when(restTemplate.exchange(
                                org.mockito.ArgumentMatchers.eq("https://whattomine.com/api/v1/coins"),
                                org.mockito.ArgumentMatchers.eq(HttpMethod.GET),
                                org.mockito.ArgumentMatchers.any(HttpEntity.class),
                                org.mockito.ArgumentMatchers.eq(String.class)))
                                .thenReturn(ResponseEntity.ok(body));

                Criptomoneda bitcoin = new Criptomoneda();
                bitcoin.setAssetId("bitcoin");
                when(criptomonedaRepository.findLatestPriceByAssetId("bitcoin"))
                                .thenReturn(Optional.of(BigDecimal.valueOf(50000)));

                localService.refreshMetricasMinado();

                ArgumentCaptor<List<MetricasMinado>> captor = ArgumentCaptor.forClass(List.class);
                verify(repository).deleteAll();
                verify(repository).saveAll(captor.capture());

                assertEquals(0, captor.getValue().size());
        }

        @Test
        void refreshMetricasMinadoThrowsWhenWhatToMineReturnsBlankBody() {
                MetricasMinadoService localService = new MetricasMinadoService(repository, criptomonedaRepository,
                                restTemplate,
                                "test-key");

                when(restTemplate.exchange(
                                org.mockito.ArgumentMatchers.eq("https://whattomine.com/api/v1/coins"),
                                org.mockito.ArgumentMatchers.eq(HttpMethod.GET),
                                org.mockito.ArgumentMatchers.any(HttpEntity.class),
                                org.mockito.ArgumentMatchers.eq(String.class)))
                                .thenReturn(ResponseEntity.ok("   "));

                assertThrows(ResponseStatusException.class, localService::refreshMetricasMinado);
        }

        @Test
        @SuppressWarnings("unchecked")
        void refreshMetricasMinadoUsesExchangeFallbackWhenLatestAssetPriceIsMissing() {
                MetricasMinadoService localService = new MetricasMinadoService(repository, criptomonedaRepository,
                                restTemplate,
                                "test-key");

                String body = "["
                                + "{"
                                + "\"id\":2,"
                                + "\"name\":\"Litecoin\","
                                + "\"algorithm\":\"Scrypt\","
                                + "\"nethash\":1000,"
                                + "\"block_time\":\"60\","
                                + "\"block_reward24\":\"2.5\","
                                + "\"exchanges\":[{\"price\":0.5}]"
                                + "}"
                                + "]";

                when(restTemplate.exchange(
                                org.mockito.ArgumentMatchers.eq("https://whattomine.com/api/v1/coins"),
                                org.mockito.ArgumentMatchers.eq(HttpMethod.GET),
                                org.mockito.ArgumentMatchers.any(HttpEntity.class),
                                org.mockito.ArgumentMatchers.eq(String.class)))
                                .thenReturn(ResponseEntity.ok(body));

                Criptomoneda bitcoin = new Criptomoneda();
                bitcoin.setAssetId("bitcoin");
                when(criptomonedaRepository.findLatestPriceByAssetId("bitcoin"))
                                .thenReturn(Optional.of(BigDecimal.valueOf(50000)));

                Criptomoneda litecoin = new Criptomoneda();
                litecoin.setAssetId("litecoin");
                when(criptomonedaRepository.findByNameIgnoreCase("Litecoin")).thenReturn(Optional.of(litecoin));
                when(criptomonedaRepository.findLatestPriceByAssetId("litecoin")).thenReturn(Optional.empty());

                localService.refreshMetricasMinado();

                ArgumentCaptor<List<MetricasMinado>> captor = ArgumentCaptor.forClass(List.class);
                verify(repository).deleteAll();
                verify(repository).saveAll(captor.capture());

                List<MetricasMinado> saved = captor.getValue();
                assertEquals(1, saved.size());
                assertEquals("Litecoin", saved.get(0).getNombreMoneda());
                assertEquals(25000.0, saved.get(0).getPrecioActualEur());
        }

        @Test
        @SuppressWarnings("unchecked")
        void refreshMetricasMinadoSkipsCoinsWithInvalidFieldsAndKeepsValidOne() {
                MetricasMinadoService localService = new MetricasMinadoService(repository, criptomonedaRepository,
                                restTemplate,
                                "test-key");

                String body = "["
                                + "{"
                                + "\"id\":0,"
                                + "\"name\":\"BadIdCoin\","
                                + "\"algorithm\":\"Scrypt\","
                                + "\"nethash\":1000,"
                                + "\"block_time\":\"60\","
                                + "\"block_reward24\":\"2.5\","
                                + "\"exchanges\":[{\"price\":0.5}]"
                                + "},"
                                + "{"
                                + "\"id\":3,"
                                + "\"name\":\"\","
                                + "\"algorithm\":\"Scrypt\","
                                + "\"nethash\":1000,"
                                + "\"block_time\":\"60\","
                                + "\"block_reward24\":\"2.5\","
                                + "\"exchanges\":[{\"price\":0.5}]"
                                + "},"
                                + "{"
                                + "\"id\":4,"
                                + "\"name\":\"NoAlgoCoin\","
                                + "\"algorithm\":\"\","
                                + "\"nethash\":1000,"
                                + "\"block_time\":\"60\","
                                + "\"block_reward24\":\"2.5\","
                                + "\"exchanges\":[{\"price\":0.5}]"
                                + "},"
                                + "{"
                                + "\"id\":5,"
                                + "\"name\":\"BadNumbersCoin\","
                                + "\"algorithm\":\"Scrypt\","
                                + "\"nethash\":0,"
                                + "\"block_time\":\"0\","
                                + "\"block_reward24\":\"0\","
                                + "\"exchanges\":[{\"price\":0.5}]"
                                + "},"
                                + "{"
                                + "\"id\":6,"
                                + "\"name\":\"NoPriceCoin\","
                                + "\"algorithm\":\"Scrypt\","
                                + "\"nethash\":1000,"
                                + "\"block_time\":\"60\","
                                + "\"block_reward24\":\"2.5\","
                                + "\"exchanges\":[{}]"
                                + "},"
                                + "{"
                                + "\"id\":7,"
                                + "\"name\":\"ValidCoin\","
                                + "\"algorithm\":\"Scrypt\","
                                + "\"nethash\":1000,"
                                + "\"block_time\":\"60\","
                                + "\"block_reward24\":\"2.5\","
                                + "\"exchanges\":[{\"price\":0.5}]"
                                + "}"
                                + "]";

                when(restTemplate.exchange(
                                org.mockito.ArgumentMatchers.eq("https://whattomine.com/api/v1/coins"),
                                org.mockito.ArgumentMatchers.eq(HttpMethod.GET),
                                org.mockito.ArgumentMatchers.any(HttpEntity.class),
                                org.mockito.ArgumentMatchers.eq(String.class)))
                                .thenReturn(ResponseEntity.ok(body));

                Criptomoneda bitcoin = new Criptomoneda();
                bitcoin.setAssetId("bitcoin");
                when(criptomonedaRepository.findLatestPriceByAssetId("bitcoin"))
                                .thenReturn(Optional.of(BigDecimal.valueOf(50000)));

                Criptomoneda validCoin = new Criptomoneda();
                validCoin.setAssetId("validcoin");
                when(criptomonedaRepository.findByNameIgnoreCase("ValidCoin")).thenReturn(Optional.of(validCoin));
                when(criptomonedaRepository.findLatestPriceByAssetId("validcoin"))
                                .thenReturn(Optional.of(BigDecimal.valueOf(123.45)));

                localService.refreshMetricasMinado();

                ArgumentCaptor<List<MetricasMinado>> captor = ArgumentCaptor.forClass(List.class);
                verify(repository).deleteAll();
                verify(repository).saveAll(captor.capture());

                List<MetricasMinado> saved = captor.getValue();
                assertEquals(1, saved.size());
                assertEquals("ValidCoin", saved.get(0).getNombreMoneda());
                assertEquals(123.45, saved.get(0).getPrecioActualEur());
        }

        @Test
        void findAllMetricasMinadoReturnsRepositoryResults() {
                when(repository.findAll()).thenReturn(List.of(new MetricasMinado()));

                assertEquals(1, service.findAllMetricasMinado().size());
        }

        @Test
        void findAllMetricasMinadoThrowsWhenEmpty() {
                when(repository.findAll()).thenReturn(List.of());

                assertThrows(ResponseStatusException.class, service::findAllMetricasMinado);
        }

        @Test
        void findAllNombresMonedasParaCalculoReturnsRepositoryResults() {
                when(repository.findAllNombresMonedasParaCalculo()).thenReturn(List.of("BTC", "ETH"));

                assertEquals(List.of("BTC", "ETH"), service.findAllNombresMonedasParaCalculo());
        }

        @Test
        void findAllMonedasParaCalculoConAlgoritmoGroupsAlgorithmsByCoin() {
                MetricasMinado btc1 = new MetricasMinado(1, "BTC", "SHA-256", 100L, 10.0, 3.0, 1.0);
                MetricasMinado btc2 = new MetricasMinado(2, "BTC", "SHA-256", 110L, 11.0, 4.0, 1.1);
                MetricasMinado eth = new MetricasMinado(3, "ETH", "Ethash", 200L, 20.0, 2.0, 2.0);
                when(repository.findAll()).thenReturn(List.of(btc1, btc2, eth));

                List<MonedaAlgoritmosDto> result = service.findAllMonedasParaCalculoConAlgoritmo();

                assertEquals(2, result.size());
                MonedaAlgoritmosDto btc = result.stream().filter(dto -> "BTC".equals(dto.getNombre())).findFirst()
                                .orElseThrow();
                MonedaAlgoritmosDto ethDto = result.stream().filter(dto -> "ETH".equals(dto.getNombre())).findFirst()
                                .orElseThrow();
                assertEquals(1, btc.getAlgoritmos().size());
                assertEquals(1, ethDto.getAlgoritmos().size());
                assertEquals(true, btc.getAlgoritmos().contains("SHA-256"));
                assertEquals(true, ethDto.getAlgoritmos().contains("Ethash"));
        }

        @Test
        void findAllMonedasParaCalculoConAlgoritmoThrowsWhenEmpty() {
                when(repository.findAll()).thenReturn(List.of());

                assertThrows(ResponseStatusException.class, service::findAllMonedasParaCalculoConAlgoritmo);
        }

        @Test
        void findMetricasByNombreReturnsRepositoryResult() {
                MetricasMinado metrica = new MetricasMinado(1, "BTC", "SHA-256", 100L, 10.0, 3.0, 1.0);
                when(repository.findByNombreMonedaIgnoreCase("BTC")).thenReturn(Optional.of(metrica));

                assertEquals("BTC", service.findMetricasByNombre("BTC").getNombreMoneda());
        }

        @Test
        void findMetricasByNombreRejectsBlankInput() {
                assertThrows(ResponseStatusException.class, () -> service.findMetricasByNombre("   "));
        }

        @Test
        void findMetricasByNombreThrowsWhenMissing() {
                when(repository.findByNombreMonedaIgnoreCase("BTC")).thenReturn(Optional.empty());

                assertThrows(ResponseStatusException.class, () -> service.findMetricasByNombre("BTC"));
        }

        @Test
        void findNombresParaCalculoThrowsWhenMissing() {
                when(repository.findAllNombresMonedasParaCalculo()).thenReturn(List.of());

                assertThrows(ResponseStatusException.class, () -> service.findAllNombresMonedasParaCalculo());
        }
}