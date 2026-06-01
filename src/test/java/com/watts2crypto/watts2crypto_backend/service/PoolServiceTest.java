package com.watts2crypto.watts2crypto_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.watts2crypto.watts2crypto_backend.models.Pool;
import com.watts2crypto.watts2crypto_backend.repositories.PoolMonedaComisionRepository;
import com.watts2crypto.watts2crypto_backend.repositories.PoolRepository;

@ExtendWith(MockitoExtension.class)
class PoolServiceTest {

    @Mock
    private PoolRepository repository;

    @Mock
    private PoolMonedaComisionRepository poolMonedaComisionRepository;

    @InjectMocks
    private PoolService service;

    @Test
    void refreshPoolsClearsCommissionTableAndSkipsPoolsWithoutSlug() {
        Pool pool = new Pool("Poolin", Set.of(Pool.EsquemaDePago.PPS), List.of("EU"), null);
        when(repository.findAll()).thenReturn(List.of(pool));
        doNothing().when(poolMonedaComisionRepository).deleteAll();

        service.refreshPools();

        verify(poolMonedaComisionRepository).deleteAll();
        verify(repository).findAll();
        verify(poolMonedaComisionRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void refreshPoolsScrapesAndSavesCommissionWhenPoolHasSlug() throws IOException {
        Pool pool = new Pool("Poolin", Set.of(Pool.EsquemaDePago.PPS), List.of("EU"), "poolin");
        pool.setId(12L);
        when(repository.findAll()).thenReturn(List.of(pool));
        when(poolMonedaComisionRepository.findByPoolIdAndMoneda(12L, "BTC")).thenReturn(Optional.empty());
        doNothing().when(poolMonedaComisionRepository).deleteAll();

        Document document = Jsoup.parse("<html><body><div class='w3-row estimate'>"
                + "<span class='brand'>BTC</span>"
                + "<div class='w3-col l2 m4 s4'><div class='estimates'>2.5%</div></div>"
                + "</div></body></html>");

        Connection connection = mock(Connection.class);
        try (var jsoup = mockStatic(Jsoup.class)) {
            jsoup.when(() -> Jsoup.connect("https://www.hashrate.no/pools/poolin")).thenReturn(connection);
            when(connection.userAgent(org.mockito.ArgumentMatchers.anyString())).thenReturn(connection);
            when(connection.timeout(org.mockito.ArgumentMatchers.anyInt())).thenReturn(connection);
            when(connection.get()).thenReturn(document);

            service.refreshPools();
        }

        verify(poolMonedaComisionRepository).save(org.mockito.ArgumentMatchers.argThat(detalle ->
                detalle.getPool() == pool
                        && "BTC".equals(detalle.getMoneda())
                        && Double.valueOf(2.5).equals(detalle.getComision())));
    }

    @Test
    void refreshPoolsSkipsInvalidRowsAndSavesOnlyValidCommission() throws IOException {
        Pool pool = new Pool("Poolin", Set.of(Pool.EsquemaDePago.PPS), List.of("EU"), "poolin");
        pool.setId(12L);
        when(repository.findAll()).thenReturn(List.of(pool));
        doNothing().when(poolMonedaComisionRepository).deleteAll();

        Document document = Jsoup.parse("<html><body>"
                + "<div class='w3-row estimate'>"
                + "<span class='brand'></span>"
                + "<div class='w3-col l2 m4 s4'><div class='estimates'>2.5%</div></div>"
                + "</div>"
                + "<div class='w3-row estimate'>"
                + "<span class='brand'>ETH</span>"
                + "<div class='w3-col l2 m4 s4'><div class='estimates'>%</div></div>"
                + "</div>"
                + "<div class='w3-row estimate'>"
                + "<span class='brand'>LTC</span>"
                + "</div>"
                + "<div class='w3-row estimate'>"
                + "<span class='brand'>BTC</span>"
                + "<div class='w3-col l2 m4 s4'><div class='estimates'>2.5%</div></div>"
                + "</div>"
                + "</body></html>");

        Connection connection = mock(Connection.class);
        try (var jsoup = mockStatic(Jsoup.class)) {
            jsoup.when(() -> Jsoup.connect("https://www.hashrate.no/pools/poolin")).thenReturn(connection);
            when(connection.userAgent(org.mockito.ArgumentMatchers.anyString())).thenReturn(connection);
            when(connection.timeout(org.mockito.ArgumentMatchers.anyInt())).thenReturn(connection);
            when(connection.get()).thenReturn(document);

            service.refreshPools();
        }

        verify(poolMonedaComisionRepository).save(org.mockito.ArgumentMatchers.argThat(detalle ->
                detalle.getPool() == pool
                        && "BTC".equals(detalle.getMoneda())
                        && Double.valueOf(2.5).equals(detalle.getComision())));
        verify(poolMonedaComisionRepository, never()).save(org.mockito.ArgumentMatchers.argThat(detalle ->
                "ETH".equals(detalle.getMoneda()) || "LTC".equals(detalle.getMoneda())));
    }

    @Test
    void findAllReturnsRepositoryResults() {
        when(repository.findAll()).thenReturn(List.of(new Pool()));

        assertEquals(1, service.findAll().size());
    }

    @Test
    void findAllThrowsWhenRepositoryIsEmpty() {
        when(repository.findAll()).thenReturn(List.of());

        assertThrows(ResponseStatusException.class, service::findAll);
    }

    @Test
    void findAllThrowsWhenRepositoryFails() {
        when(repository.findAll()).thenThrow(new RuntimeException("boom"));

        assertThrows(ResponseStatusException.class, () -> service.findAll());
    }

    @Test
    void findByNameReturnsRepositoryResult() {
        Pool pool = new Pool();
        pool.setNombre("Poolin");
        when(repository.findByName("Poolin")).thenReturn(Optional.of(pool));

        assertEquals("Poolin", service.findByName("Poolin").getNombre());
    }

    @Test
    void findByNameThrowsWhenMissing() {
        when(repository.findByName("Missing")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> service.findByName("Missing"));
    }

    @Test
    void findAllNamesReturnsRepositoryResults() {
        when(repository.findAllNames()).thenReturn(List.of("Poolin", "F2Pool"));

        assertEquals(List.of("Poolin", "F2Pool"), service.findAllNames());
    }

    @Test
    void findAllNamesThrowsWhenRepositoryReturnsEmpty() {
        when(repository.findAllNames()).thenReturn(List.of());

        assertThrows(ResponseStatusException.class, () -> service.findAllNames());
    }

    @Test
    void findAvailablePoolsByAlgorithmReturnsRepositoryResults() {
        when(repository.findNamesByAlgorithm("SHA-256")).thenReturn(List.of("Poolin"));

        assertEquals(List.of("Poolin"), service.findAvailablePoolsByAlgorithm("SHA-256"));
    }

    @Test
    void findAvailablePoolsByAlgorithmThrowsWhenEmpty() {
        when(repository.findNamesByAlgorithm("SHA-256")).thenReturn(List.of());

        assertThrows(ResponseStatusException.class, () -> service.findAvailablePoolsByAlgorithm("SHA-256"));
    }
}