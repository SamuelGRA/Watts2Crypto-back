package com.watts2crypto.watts2crypto_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import com.watts2crypto.watts2crypto_backend.models.Gpu;
import com.watts2crypto.watts2crypto_backend.models.RendimientoAlgoritmo;
import com.watts2crypto.watts2crypto_backend.repositories.GpuRepository;

@ExtendWith(MockitoExtension.class)
class GpuServiceTest {

    @Mock
    private GpuRepository repository;

    @Mock
    private org.springframework.web.client.RestTemplate restTemplate;

    @InjectMocks
    private GpuService service;

    @Test
    @SuppressWarnings("unchecked")
    void refreshGpusParsesApiResponseAndSkipsOlderDuplicates() {
        String body = """
                [
                  {
                    "name":"RTX 3080",
                    "release_date":"2023-01-01",
                    "algorithms":[{"name":"Old","hashrate":"1.0","power":1}]
                  },
                  {
                    "name":"RTX 3080",
                    "release_date":"2024-05-01",
                    "algorithms":[
                      {"name":"Ethash","hashrate":"100.5","power":220},
                      {"name":"KawPow","hashrate":"90.0","power":200}
                    ]
                  },
                  {
                    "name":"RTX 4090",
                    "release_date":"2024-06-01",
                    "algorithms":[]
                  }
                ]
                """;
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(ResponseEntity.ok(body));

        service.refreshGpus();

        verify(repository).deleteAll();
        ArgumentCaptor<List<Gpu>> captor = ArgumentCaptor.forClass(List.class);
        verify(repository).saveAll(captor.capture());
        List<Gpu> gpus = captor.getValue();
        assertEquals(1, gpus.size());
        Gpu gpu = gpus.get(0);
        assertEquals("RTX 3080", gpu.getNombre());
        assertEquals(1, gpu.getAlgorithms().size());
        assertEquals(1, gpu.getConsumoNominal());
        assertEquals(1.0, gpu.getHashrateNominal());
    }

      @Test
      void refreshGpusThrowsWhenApiReturnsBlankBody() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
            .thenReturn(ResponseEntity.ok("   "));

        assertThrows(ResponseStatusException.class, () -> service.refreshGpus());
      }

    @Test
    void findAllGpusReturnsRepositoryResults() {
        when(repository.findAll()).thenReturn(List.of(new Gpu()));

        assertEquals(1, service.findAllGpus().size());
    }

      @Test
      void findAllGpusThrowsWhenRepositoryFails() {
        when(repository.findAll()).thenThrow(new RuntimeException("boom"));

        assertThrows(ResponseStatusException.class, () -> service.findAllGpus());
      }

    @Test
    void findGpuByNameReturnsRepositoryResult() {
        Gpu gpu = new Gpu();
        gpu.setNombre("RTX 3080");
        when(repository.findByNameIgnoreCase("RTX 3080")).thenReturn(Optional.of(gpu));

        assertEquals("RTX 3080", service.findGpuByName("RTX 3080").getNombre());
    }

      @Test
      void findGpuByNameThrowsWhenMissing() {
        when(repository.findByNameIgnoreCase("Missing")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> service.findGpuByName("Missing"));
      }

    @Test
    void findAllGpuNamesReturnsOptionalList() {
        when(repository.findAllNames()).thenReturn(Optional.of(List.of("RTX 3080", "RTX 4090")));

        assertEquals(List.of("RTX 3080", "RTX 4090"), service.findAllGpuNames());
    }

      @Test
      void findAllGpuNamesThrowsWhenRepositoryReturnsEmpty() {
        when(repository.findAllNames()).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> service.findAllGpuNames());
      }

    @Test
    void findHashrateYConsumoPorNombreYAlgoritmoReturnsRepositoryValue() {
        RendimientoAlgoritmo rendimiento = new RendimientoAlgoritmo(100.0, 200);
        when(repository.findHashrateAndPowerByGpuAndAlgorithm("RTX 3080", "Ethash")).thenReturn(Optional.of(rendimiento));

        assertEquals(100.0, service.findHashrateYConsumoPorNombreYAlgoritmo("RTX 3080", "Ethash").getHashrate());
    }

      @Test
      void findHashrateYConsumoPorNombreYAlgoritmoThrowsWhenMissing() {
        when(repository.findHashrateAndPowerByGpuAndAlgorithm("Missing", "Ethash")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
            () -> service.findHashrateYConsumoPorNombreYAlgoritmo("Missing", "Ethash"));
      }

    @Test
    void findNamesByAlgorithmRejectsBlankAlgorithm() {
        assertThrows(ResponseStatusException.class, () -> service.findNamesByAlgorithm("   "));
    }

    @Test
    void findNamesByAlgorithmReturnsRepositoryResults() {
        when(repository.findNamesByAlgorithm("Ethash")).thenReturn(List.of("RTX 3080"));

        assertEquals(List.of("RTX 3080"), service.findNamesByAlgorithm(" Ethash "));
    }

      @Test
      void findNamesByAlgorithmThrowsWhenRepositoryFails() {
        when(repository.findNamesByAlgorithm("Ethash")).thenThrow(new RuntimeException("boom"));

        assertThrows(ResponseStatusException.class, () -> service.findNamesByAlgorithm("Ethash"));
      }
}