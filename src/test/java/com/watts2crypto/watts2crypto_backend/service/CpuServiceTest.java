package com.watts2crypto.watts2crypto_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.watts2crypto.watts2crypto_backend.models.Cpu;
import com.watts2crypto.watts2crypto_backend.repositories.CpuRepository;

@ExtendWith(MockitoExtension.class)
class CpuServiceTest {

    @Mock
    private CpuRepository repository;

    @Mock
    private Connection connection;

    @InjectMocks
    private CpuService service;

    @Test
    void refreshCpusScrapesHtmlTablesAndPersistsResults() throws Exception {
        String html = """
                <html><body>
                  <table>
                    <tr><td>Ryzen 9 7950X</td><td>100.0 Kh/s</td></tr>
                    <tr><td>Antminer S19</td><td>999.0 Kh/s</td></tr>
                  </table>
                  <table>
                    <tr><td>Ryzen 9 7950X</td><td>50.0 Kh/W</td></tr>
                  </table>
                </body></html>
                """;
        Document document = Jsoup.parse(html);
        when(connection.userAgent(anyString())).thenReturn(connection);
        when(connection.timeout(anyInt())).thenReturn(connection);
        when(connection.get()).thenReturn(document);

        try (MockedStatic<Jsoup> jsoup = mockStatic(Jsoup.class)) {
            jsoup.when(() -> Jsoup.connect("https://www.hashrate.no/coins/XMR/benchmarks")).thenReturn(connection);

            service.refreshCpus();
        }

        verify(repository).deleteAll();
        verify(repository).save(org.mockito.ArgumentMatchers.argThat(cpu -> {
            Cpu saved = (Cpu) cpu;
            return "Ryzen 9 7950X".equals(saved.getNombre())
                    && saved.getHashrate() == 100000
                    && saved.getConsumoNominal() == 2;
        }));
    }

    @Test
    void findAllCpusReturnsRepositoryResults() {
        when(repository.findAll()).thenReturn(List.of(new Cpu()));

        assertEquals(1, service.findAllCpus().size());
    }

    @Test
    void findAllCpusThrowsWhenRepositoryFails() {
        when(repository.findAll()).thenThrow(new RuntimeException("boom"));

        assertThrows(ResponseStatusException.class, () -> service.findAllCpus());
    }

    @Test
    void findAllCpuNamesReturnsRepositoryResults() {
        when(repository.findAllNames()).thenReturn(Optional.of(List.of("Ryzen 9 7950X")));

        assertEquals(List.of("Ryzen 9 7950X"), service.findAllCpuNames());
    }

    @Test
    void findAllCpuNamesThrowsWhenRepositoryReturnsEmpty() {
        when(repository.findAllNames()).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> service.findAllCpuNames());
    }

    @Test
    void findCpuByNameReturnsRepositoryResult() {
        Cpu cpu = new Cpu();
        cpu.setNombre("Ryzen 9 7950X");
        when(repository.findByNameIgnoreCase("Ryzen 9 7950X")).thenReturn(Optional.of(cpu));

        assertEquals("Ryzen 9 7950X", service.findCpuByName("Ryzen 9 7950X").getNombre());
    }

    @Test
    void findCpuByNameThrowsWhenMissing() {
        when(repository.findByNameIgnoreCase("Missing")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> service.findCpuByName("Missing"));
    }

    @Test
    void findNamesByAlgorithmRejectsNonRandomx() {
        assertThrows(ResponseStatusException.class, () -> service.findNamesByAlgorithm("Ethash"));
    }

    @Test
    void findNamesByAlgorithmReturnsRepositoryNamesForRandomx() {
        when(repository.findAllNames()).thenReturn(Optional.of(List.of("Ryzen 9 7950X")));

        assertEquals(List.of("Ryzen 9 7950X"), service.findNamesByAlgorithm("RandomX"));
    }
}