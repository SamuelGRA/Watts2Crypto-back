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

import com.watts2crypto.watts2crypto_backend.models.Asic;
import com.watts2crypto.watts2crypto_backend.models.RendimientoAlgoritmo;
import com.watts2crypto.watts2crypto_backend.repositories.AsicRepository;

@ExtendWith(MockitoExtension.class)
class AsicServiceTest {

    @Mock
    private AsicRepository repository;

    @Mock
    private org.springframework.web.client.RestTemplate restTemplate;

    @InjectMocks
    private AsicService service;

    @Test
    @SuppressWarnings("unchecked")
    void refreshAsicsParsesApiResponseAndSkipsOlderDuplicates() {
        String body = """
                [
                  {
                    "name":"Antminer S19",
                    "release_date":"2023-01-01",
                    "algorithms":[{"name":"Old","hashrate":"1.0","power":1}]
                  },
                  {
                    "name":"Antminer S19",
                    "release_date":"2024-05-01",
                    "algorithms":[
                      {"name":"SHA-256","hashrate":"100.0","power":3250},
                      {"name":"SHA-256 low power","hashrate":"90.0","power":3000}
                    ]
                  }
                ]
                """;
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(ResponseEntity.ok(body));

        service.refreshAsics();

        verify(repository).deleteAll();
        ArgumentCaptor<List<Asic>> captor = ArgumentCaptor.forClass(List.class);
        verify(repository).saveAll(captor.capture());
        List<Asic> asics = captor.getValue();
        assertEquals(1, asics.size());
        Asic asic = asics.get(0);
        assertEquals("Antminer S19", asic.getNombre());
        assertEquals(1, asic.getAlgorithms().size());
        assertEquals(1, asic.getConsumoNominal());
        assertEquals(1.0, asic.getHashrateNominal());
    }

      @Test
      void refreshAsicsThrowsWhenApiReturnsBlankBody() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
            .thenReturn(ResponseEntity.ok("   "));

        assertThrows(ResponseStatusException.class, () -> service.refreshAsics());
      }

      @Test
      @SuppressWarnings("unchecked")
      void refreshAsicsSkipsInvalidRowsAndKeepsValidOne() {
        String body = "["
            + "{" 
            + "\"name\":\"BrokenAlgoAsic\","
            + "\"release_date\":\"2024-05-01\","
            + "\"algorithms\":[]"
            + "},"
            + "{"
            + "\"name\":\"Antminer S19\","
            + "\"release_date\":\"2023-01-01\","
            + "\"algorithms\":[{" +
            "\"name\":\"Old\",\"hashrate\":\"1.0\",\"power\":1}]"
            + "},"
            + "{"
            + "\"name\":\"Antminer S19\","
            + "\"release_date\":\"2024-05-01\","
            + "\"algorithms\":["
            + "{\"name\":\"SHA-256\",\"hashrate\":\"100.0\",\"power\":3250},"
            + "{\"name\":\"SHA-256 low power\",\"hashrate\":\"90.0\",\"power\":3000}"
            + "]"
            + "}"
            + "]";

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
            .thenReturn(ResponseEntity.ok(body));

        service.refreshAsics();

        verify(repository).deleteAll();
        ArgumentCaptor<List<Asic>> captor = ArgumentCaptor.forClass(List.class);
        verify(repository).saveAll(captor.capture());
        List<Asic> asics = captor.getValue();
        assertEquals(1, asics.size());
        Asic asic = asics.get(0);
        assertEquals("Antminer S19", asic.getNombre());
        assertEquals(1, asic.getAlgorithms().size());
        assertEquals(1, asic.getConsumoNominal());
        assertEquals(1.0, asic.getHashrateNominal());
      }

    @Test
    void findAllAsicsReturnsRepositoryResults() {
        when(repository.findAll()).thenReturn(List.of(new Asic()));

        assertEquals(1, service.findAllAsics().size());
    }

      @Test
      void findAllAsicsThrowsWhenRepositoryFails() {
        when(repository.findAll()).thenThrow(new RuntimeException("boom"));

        assertThrows(ResponseStatusException.class, () -> service.findAllAsics());
      }

    @Test
    void findAsicByNameReturnsRepositoryResult() {
        Asic asic = new Asic();
        asic.setNombre("Antminer S19");
        when(repository.findByNameIgnoreCase("Antminer S19")).thenReturn(Optional.of(asic));

        assertEquals("Antminer S19", service.findAsicByName("Antminer S19").getNombre());
    }

      @Test
      void findAsicByNameThrowsWhenMissing() {
        when(repository.findByNameIgnoreCase("Missing")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> service.findAsicByName("Missing"));
      }

    @Test
    void findAllAsicNamesReturnsOptionalList() {
        when(repository.findAllNames()).thenReturn(Optional.of(List.of("Antminer S19", "Whatsminer M30S")));

        assertEquals(List.of("Antminer S19", "Whatsminer M30S"), service.findAllAsicNames());
    }

      @Test
      void findAllAsicNamesThrowsWhenRepositoryReturnsEmpty() {
        when(repository.findAllNames()).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> service.findAllAsicNames());
      }

    @Test
    void findHashrateYConsumoPorNombreYAlgoritmoReturnsRepositoryValue() {
        RendimientoAlgoritmo rendimiento = new RendimientoAlgoritmo(100.0, 3250);
        when(repository.findHashrateAndPowerByAsicAndAlgorithm("Antminer S19", "SHA-256")).thenReturn(Optional.of(rendimiento));

        assertEquals(100.0, service.findHashrateYConsumoPorNombreYAlgoritmo("Antminer S19", "SHA-256").getHashrate());
    }

      @Test
      void findHashrateYConsumoPorNombreYAlgoritmoThrowsWhenMissing() {
        when(repository.findHashrateAndPowerByAsicAndAlgorithm("Missing", "SHA-256")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
            () -> service.findHashrateYConsumoPorNombreYAlgoritmo("Missing", "SHA-256"));
      }

    @Test
    void findNamesByAlgorithmRejectsBlankAlgorithm() {
        assertThrows(ResponseStatusException.class, () -> service.findNamesByAlgorithm("   "));
    }

    @Test
    void findNamesByAlgorithmReturnsRepositoryResults() {
        when(repository.findNamesByAlgorithm("SHA-256")).thenReturn(List.of("Antminer S19"));

        assertEquals(List.of("Antminer S19"), service.findNamesByAlgorithm(" SHA-256 "));
    }

      @Test
      void findNamesByAlgorithmThrowsWhenRepositoryReturnsEmpty() {
        when(repository.findNamesByAlgorithm("SHA-256")).thenReturn(List.of());

        assertThrows(ResponseStatusException.class, () -> service.findNamesByAlgorithm("SHA-256"));
      }

      @Test
      void findNamesByAlgorithmThrowsWhenRepositoryFails() {
        when(repository.findNamesByAlgorithm("SHA-256")).thenThrow(new RuntimeException("boom"));

        assertThrows(ResponseStatusException.class, () -> service.findNamesByAlgorithm("SHA-256"));
      }
}