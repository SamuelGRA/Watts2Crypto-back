package com.watts2crypto.watts2crypto_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.test.util.ReflectionTestUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.watts2crypto.watts2crypto_backend.models.Software;
import com.watts2crypto.watts2crypto_backend.models.Software.HardwareUsable;
import com.watts2crypto.watts2crypto_backend.models.Software.SistemaOperativo;
import com.watts2crypto.watts2crypto_backend.repositories.SoftwareAlgoritmoMonedaRepository;
import com.watts2crypto.watts2crypto_backend.repositories.SoftwareRepository;

@ExtendWith(MockitoExtension.class)
class SoftwareServiceTest {

    @Mock
    private SoftwareRepository repository;

    @Mock
    private SoftwareAlgoritmoMonedaRepository softwareAlgoritmoMonedaRepository;

    @InjectMocks
    private SoftwareService service;

    @Test
    void findAllReturnsRepositoryResults() {
        Software software = new Software("PhoenixMiner", Set.of(HardwareUsable.GPU), Set.of(SistemaOperativo.WINDOWS),
                "phoenixminer");
        when(repository.findAll()).thenReturn(List.of(software));

        List<Software> result = service.findAll();

        assertEquals(1, result.size());
        assertEquals("PhoenixMiner", result.get(0).getNombre());
    }

    @Test
    void findAllThrowsNotFoundWhenRepositoryIsEmpty() {
        when(repository.findAll()).thenReturn(List.of());

        assertThrows(ResponseStatusException.class, service::findAll);
    }

    @Test
    void findAllThrowsWhenRepositoryFails() {
        when(repository.findAll()).thenThrow(new RuntimeException("boom"));

        assertThrows(ResponseStatusException.class, () -> service.findAll());
    }

    @Test
    void findByNameReturnsMatch() {
        Software software = new Software("BzMiner", Set.of(HardwareUsable.GPU), Set.of(SistemaOperativo.LINUX),
                "bzminer");
        when(repository.findByName("BzMiner")).thenReturn(Optional.of(software));

        Software result = service.findByName("BzMiner");

        assertEquals("BzMiner", result.getNombre());
    }

    @Test
    void findByNameThrowsNotFoundWhenMissing() {
        when(repository.findByName("Unknown")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> service.findByName("Unknown"));
    }

    @Test
    void findByNameThrowsWhenRepositoryFails() {
        when(repository.findByName("Unknown")).thenThrow(new RuntimeException("boom"));

        assertThrows(ResponseStatusException.class, () -> service.findByName("Unknown"));
    }

    @Test
    void findAllNamesReturnsRepositoryNames() {
        when(repository.findAllNames()).thenReturn(List.of("PhoenixMiner", "BzMiner"));

        assertEquals(List.of("PhoenixMiner", "BzMiner"), service.findAllNames());
    }

    @Test
    void findAllNamesThrowsNotFoundWhenEmpty() {
        when(repository.findAllNames()).thenReturn(List.of());

        assertThrows(ResponseStatusException.class, service::findAllNames);
    }

    @Test
    void findAllNamesThrowsWhenRepositoryFails() {
        when(repository.findAllNames()).thenThrow(new RuntimeException("boom"));

        assertThrows(ResponseStatusException.class, () -> service.findAllNames());
    }

    @Test
    void findAvailableSoftwaresByAlgorithmReturnsNames() {
        when(repository.findNamesByAlgorithm("KawPow")).thenReturn(List.of("PhoenixMiner"));

        assertEquals(List.of("PhoenixMiner"), service.findAvailableSoftwaresByAlgorithm("KawPow"));
    }

    @Test
    void findAvailableSoftwaresByAlgorithmThrowsNotFoundWhenEmpty() {
        when(repository.findNamesByAlgorithm("KawPow")).thenReturn(List.of());

        assertThrows(ResponseStatusException.class, () -> service.findAvailableSoftwaresByAlgorithm("KawPow"));
    }

    @Test
    void findAvailableSoftwaresByAlgorithmThrowsWhenRepositoryFails() {
        when(repository.findNamesByAlgorithm("KawPow")).thenThrow(new RuntimeException("boom"));

        assertThrows(ResponseStatusException.class, () -> service.findAvailableSoftwaresByAlgorithm("KawPow"));
    }

    @Test
    void refreshSoftwareClearsExistingDataAndSkipsSoftwareWithoutSlug() {
        Software software = new Software("NoSlugMiner", Set.of(HardwareUsable.GPU), Set.of(SistemaOperativo.WINDOWS),
                null);
        when(repository.findAll()).thenReturn(List.of(software));
        doNothing().when(softwareAlgoritmoMonedaRepository).deleteAll();

        service.refreshSoftware();

        verify(softwareAlgoritmoMonedaRepository).deleteAll();
        verify(repository).findAll();
        verify(softwareAlgoritmoMonedaRepository, never()).save(ArgumentMatchers.any());
    }

    @Test
    void refreshSoftwareScrapesAndSavesCommissionWhenSoftwareHasSlug() throws IOException {
        Software software = new Software("PhoenixMiner", Set.of(HardwareUsable.GPU), Set.of(SistemaOperativo.WINDOWS),
                "phoenixminer");
        ReflectionTestUtils.setField(software, "id", 21L);
        when(repository.findAll()).thenReturn(List.of(software));
        when(softwareAlgoritmoMonedaRepository.findBySoftwareIdAndMoneda(21L, "BTC")).thenReturn(Optional.empty());
        doNothing().when(softwareAlgoritmoMonedaRepository).deleteAll();

        Document document = Jsoup.parse("<html><body><div class='w3-row estimate'>"
                + "<span class='brand'>BTC</span>"
                + "<div class='w3-col l7 m7 s7'><div class='estimates'>SHA-256</div></div>"
                + "<div class='w3-col l5 m5 s5'><div class='estimates'>3.75%</div></div>"
                + "</div></body></html>");

        Connection connection = mock(Connection.class);
        try (var jsoup = mockStatic(Jsoup.class)) {
            jsoup.when(() -> Jsoup.connect("https://www.hashrate.no/miners/phoenixminer")).thenReturn(connection);
            when(connection.userAgent(org.mockito.ArgumentMatchers.anyString())).thenReturn(connection);
            when(connection.timeout(org.mockito.ArgumentMatchers.anyInt())).thenReturn(connection);
            when(connection.get()).thenReturn(document);

            service.refreshSoftware();
        }

        verify(softwareAlgoritmoMonedaRepository)
                .save(org.mockito.ArgumentMatchers.argThat(detalle -> detalle.getSoftware() == software
                        && "BTC".equals(detalle.getMoneda())
                        && "SHA-256".equals(detalle.getAlgoritmo())
                        && Double.valueOf(3.75).equals(detalle.getComision())));
    }

    @Test
    void refreshSoftwareWrapsUnexpectedErrorsAsInternalServerError() {
        doNothing().when(softwareAlgoritmoMonedaRepository).deleteAll();
        when(repository.findAll()).thenThrow(new RuntimeException("boom"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, service::refreshSoftware);

        assertEquals(500, exception.getStatusCode().value());
    }
}