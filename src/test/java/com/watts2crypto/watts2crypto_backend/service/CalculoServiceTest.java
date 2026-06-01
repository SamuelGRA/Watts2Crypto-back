package com.watts2crypto.watts2crypto_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import com.watts2crypto.watts2crypto_backend.models.Asic;
import com.watts2crypto.watts2crypto_backend.models.Cpu;
import com.watts2crypto.watts2crypto_backend.models.ElectricidadPorPais;
import com.watts2crypto.watts2crypto_backend.models.Gpu;
import com.watts2crypto.watts2crypto_backend.models.MetricasMinado;
import com.watts2crypto.watts2crypto_backend.models.Pool;
import com.watts2crypto.watts2crypto_backend.models.RendimientoAlgoritmo;
import com.watts2crypto.watts2crypto_backend.models.Software;
import com.watts2crypto.watts2crypto_backend.models.SoftwareAlgoritmoMoneda;
import com.watts2crypto.watts2crypto_backend.models.DTOs.CalculoInputDto;
import com.watts2crypto.watts2crypto_backend.models.DTOs.CalculoOutputDto;
import com.watts2crypto.watts2crypto_backend.models.DTOs.CalculoHardwareItemDto;
import com.watts2crypto.watts2crypto_backend.repositories.AsicRepository;
import com.watts2crypto.watts2crypto_backend.repositories.CpuRepository;
import com.watts2crypto.watts2crypto_backend.repositories.ElectricidadPorPaisRepository;
import com.watts2crypto.watts2crypto_backend.repositories.GpuRepository;
import com.watts2crypto.watts2crypto_backend.repositories.MetricasMinadoRepository;
import com.watts2crypto.watts2crypto_backend.repositories.PoolMonedaComisionRepository;
import com.watts2crypto.watts2crypto_backend.repositories.PoolRepository;
import com.watts2crypto.watts2crypto_backend.repositories.SoftwareAlgoritmoMonedaRepository;
import com.watts2crypto.watts2crypto_backend.repositories.SoftwareRepository;

@ExtendWith(MockitoExtension.class)
class CalculoServiceTest {

    @Mock
    private CpuRepository cpuRepository;

    @Mock
    private GpuRepository gpuRepository;

    @Mock
    private AsicRepository asicRepository;

    @Mock
    private ElectricidadPorPaisRepository electricidadPorPaisRepository;

    @Mock
    private PoolRepository poolRepository;

    @Mock
    private SoftwareRepository softwareRepository;

    @Mock
    private MetricasMinadoRepository metricasMinadoRepository;

    @Mock
    private SoftwareAlgoritmoMonedaRepository softwareAlgoritmoMonedaRepository;

    @Mock
    private PoolMonedaComisionRepository poolMonedaComisionRepository;

    @InjectMocks
    private CalculoService service;

    @Test
    void findPaisesElectricidadReturnsRepositoryResults() {
        when(electricidadPorPaisRepository.findAllPaises()).thenReturn(List.<ElectricidadPorPais>of());

        assertEquals(List.of(), service.findPaisesElectricidad());
    }

    @Test
    void findAlgoritmosSoportadosPorHardwareRejectsBlankInput() {
        assertThrows(ResponseStatusException.class,
                () -> service.findAlgoritmosSoportadosPorHardware("", "Ryzen"));
    }

    @Test
    void findAlgoritmosSoportadosPorHardwareRejectsInvalidType() {
        assertThrows(ResponseStatusException.class,
                () -> service.findAlgoritmosSoportadosPorHardware("FPGA", "Ryzen"));
    }

    @Test
    void findAlgoritmosSoportadosPorHardwareReturnsRandomXForValidCpu() {
        Cpu cpu = new Cpu("Ryzen 9", 105, 150);
        when(cpuRepository.findByNameIgnoreCase("Ryzen 9")).thenReturn(Optional.of(cpu));

        assertEquals(List.of("RandomX"), service.findAlgoritmosSoportadosPorHardware("cpu", "Ryzen 9"));
    }

    @Test
    void findAlgoritmosSoportadosPorHardwareRejectsCpuWithoutEnoughData() {
        Cpu cpu = new Cpu();
        cpu.setNombre("Ryzen 9");
        cpu.setConsumoNominal(105);
        cpu.setHashrate(null);
        when(cpuRepository.findByNameIgnoreCase("Ryzen 9")).thenReturn(Optional.of(cpu));

        assertThrows(ResponseStatusException.class,
                () -> service.findAlgoritmosSoportadosPorHardware("CPU", "Ryzen 9"));
    }

    @Test
    void findAlgoritmosSoportadosPorHardwareRejectsMissingCpu() {
        when(cpuRepository.findByNameIgnoreCase("Missing")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> service.findAlgoritmosSoportadosPorHardware("CPU", "Missing"));
    }

    @Test
    void findAlgoritmosSoportadosPorHardwareSortsGpuAlgorithms() {
        Map<String, RendimientoAlgoritmo> algorithms = Map.of(
                "Zeta", new RendimientoAlgoritmo(20.0, 200),
                "alpha", new RendimientoAlgoritmo(10.0, 100));
        Gpu gpu = new Gpu("RTX 4090", 450, 1000.0, algorithms);
        when(gpuRepository.findByNameIgnoreCase("RTX 4090")).thenReturn(Optional.of(gpu));

        assertEquals(List.of("alpha", "Zeta"), service.findAlgoritmosSoportadosPorHardware("gpu", "RTX 4090"));
    }

    @Test
    void findAlgoritmosSoportadosPorHardwareRejectsGpuWithoutAlgorithms() {
        Gpu gpu = new Gpu("RTX 4090", 450, 1000.0, Map.of());
        when(gpuRepository.findByNameIgnoreCase("RTX 4090")).thenReturn(Optional.of(gpu));

        assertThrows(ResponseStatusException.class,
                () -> service.findAlgoritmosSoportadosPorHardware("GPU", "RTX 4090"));
    }

    @Test
    void findAlgoritmosSoportadosPorHardwareSortsAsicAlgorithms() {
        Map<String, RendimientoAlgoritmo> algorithms = Map.of(
                "sha-256", new RendimientoAlgoritmo(1000.0, 2500),
                "blake3", new RendimientoAlgoritmo(2000.0, 3000));
        Asic asic = new Asic("Antminer", 2500, 1000.0, algorithms);
        when(asicRepository.findByNameIgnoreCase("Antminer")).thenReturn(Optional.of(asic));

        assertEquals(List.of("blake3", "sha-256"), service.findAlgoritmosSoportadosPorHardware("ASIC", "Antminer"));
    }

    @Test
    void calcularRentabilidadThrowsWhenInputIsNull() {
        assertThrows(ResponseStatusException.class, () -> service.calcularRentabilidad(null));
    }

    @Test
    void calcularRentabilidadThrowsWhenMonedaIsMissing() {
        CalculoInputDto input = new CalculoInputDto();

        assertThrows(ResponseStatusException.class, () -> service.calcularRentabilidad(input));
    }

    @Test
    void calcularRentabilidadThrowsWhenHardwareItemIsNull() {
        CalculoInputDto input = baseManualInput();
        List<CalculoHardwareItemDto> hardwareItems = new ArrayList<>();
        hardwareItems.add(null);
        input.setHardwareItems(hardwareItems);
        input.setHashrate(null);
        input.setConsumoW(null);
        input.setPrecioKwh(0.0);
        input.setComision(0.0);

        assertThrows(ResponseStatusException.class, () -> service.calcularRentabilidad(input));
    }

    @Test
    void calcularRentabilidadThrowsWhenHardwareItemTypeIsBlank() {
        CalculoInputDto input = baseManualInput();
        input.setHardwareItems(List.of(new CalculoHardwareItemDto("   ", "RTX 4090", 1)));
        input.setHashrate(null);
        input.setConsumoW(null);
        input.setPrecioKwh(0.0);
        input.setComision(0.0);

        assertThrows(ResponseStatusException.class, () -> service.calcularRentabilidad(input));
    }

    @Test
    void calcularRentabilidadThrowsWhenHardwareItemNameIsBlank() {
        CalculoInputDto input = baseManualInput();
        input.setHardwareItems(List.of(new CalculoHardwareItemDto("GPU", "  ", 1)));
        input.setHashrate(null);
        input.setConsumoW(null);
        input.setPrecioKwh(0.0);
        input.setComision(0.0);

        assertThrows(ResponseStatusException.class, () -> service.calcularRentabilidad(input));
    }

    @Test
    void calcularRentabilidadThrowsWhenHardwareItemQuantityIsInvalid() {
        CalculoInputDto input = baseManualInput();
        input.setHardwareItems(List.of(new CalculoHardwareItemDto("GPU", "RTX 4090", 0)));
        input.setHashrate(null);
        input.setConsumoW(null);
        input.setPrecioKwh(0.0);
        input.setComision(0.0);

        assertThrows(ResponseStatusException.class, () -> service.calcularRentabilidad(input));
    }

    @Test
    void calcularRentabilidadThrowsWhenHashrateIsInvalid() {
        CalculoInputDto input = baseManualInput();
        input.setHashrate(0.0);

        assertThrows(ResponseStatusException.class, () -> service.calcularRentabilidad(input));
    }

    @Test
    void calcularRentabilidadThrowsWhenConsumptionIsNegative() {
        CalculoInputDto input = baseManualInput();
        input.setConsumoW(-1.0);

        assertThrows(ResponseStatusException.class, () -> service.calcularRentabilidad(input));
    }

    @Test
    void calcularRentabilidadThrowsWhenPriceKwhIsNegative() {
        CalculoInputDto input = baseManualInput();
        input.setPrecioKwh(-0.01);

        assertThrows(ResponseStatusException.class, () -> service.calcularRentabilidad(input));
    }

    @Test
    void calcularRentabilidadThrowsWhenPaisIsMissingAndNoManualPrice() {
        CalculoInputDto input = baseManualInput();
        input.setPrecioKwh(null);
        input.setPais("   ");

        assertThrows(ResponseStatusException.class, () -> service.calcularRentabilidad(input));
    }

    @Test
    void calcularRentabilidadThrowsWhenCommissionIsOutOfRange() {
        CalculoInputDto input = baseManualInput();
        input.setComision(120.0);

        assertThrows(ResponseStatusException.class, () -> service.calcularRentabilidad(input));
    }

    @Test
    void calcularRentabilidadThrowsWhenInitialCostIsInvalid() {
        CalculoInputDto input = baseManualInput();
        input.setCostoInicialHardware(0.0);

        assertThrows(ResponseStatusException.class, () -> service.calcularRentabilidad(input));
    }

    @Test
    void calcularRentabilidadUsesPoolCommissionWhenComisionIsNotProvided() {
        MetricasMinado metrics = new MetricasMinado(1, "BTC", "SHA-256", 1000L, 100.0, 10.0, 2.0);
        Pool pool = new Pool("Pool BTC", Set.of(Pool.EsquemaDePago.PPS), List.of("EU"), "pool-btc");
        pool.setId(1L);

        when(metricasMinadoRepository.findByNombreMonedaIgnoreCase("BTC")).thenReturn(Optional.of(metrics));
        when(poolRepository.findByName("Pool BTC")).thenReturn(Optional.of(pool));
        when(poolMonedaComisionRepository.findByPoolIdAndMoneda(1L, "BTC"))
            .thenReturn(Optional.of(new com.watts2crypto.watts2crypto_backend.models.PoolMonedaComision(pool, "BTC", 1.75)));
        when(poolMonedaComisionRepository.findComisionByPoolIdAndMoneda(1L, "BTC")).thenReturn(Optional.of(1.75));

        CalculoInputDto input = new CalculoInputDto();
        input.setMoneda("BTC");
        input.setHashrate(100.0);
        input.setConsumoW(50.0);
        input.setPrecioKwh(0.10);
        input.setPool("Pool BTC");
        input.setCostoInicialHardware(1000.0);

        CalculoOutputDto output = service.calcularRentabilidad(input);

        assertEquals(1.75, output.getComision());
    }

    @Test
    void calcularRentabilidadUsesSoftwareCommissionWhenComisionIsNotProvided() {
        MetricasMinado metrics = new MetricasMinado(1, "BTC", "SHA-256", 1000L, 100.0, 10.0, 2.0);
        Software software = new Software("PhoenixMiner", Set.of(Software.HardwareUsable.GPU), Set.of(Software.SistemaOperativo.WINDOWS), "phoenixminer");
        ReflectionTestUtils.setField(software, "id", 2L);

        when(metricasMinadoRepository.findByNombreMonedaIgnoreCase("BTC")).thenReturn(Optional.of(metrics));
        when(softwareRepository.findByName("PhoenixMiner")).thenReturn(Optional.of(software));
        when(softwareAlgoritmoMonedaRepository.findBySoftwareIdAndMoneda(2L, "BTC"))
            .thenReturn(Optional.of(new SoftwareAlgoritmoMoneda(software, "BTC", "SHA-256", 2.25)));
        when(softwareAlgoritmoMonedaRepository.findComisionBySoftwareIdAndMoneda(2L, "BTC"))
            .thenReturn(Optional.of(2.25));

        CalculoInputDto input = new CalculoInputDto();
        input.setMoneda("BTC");
        input.setHashrate(100.0);
        input.setConsumoW(50.0);
        input.setPrecioKwh(0.10);
        input.setSoftware("PhoenixMiner");
        input.setCostoInicialHardware(1000.0);

        CalculoOutputDto output = service.calcularRentabilidad(input);

        assertEquals(2.25, output.getComision());
    }

    @Test
    void calcularRentabilidadThrowsWhenHardwareAndManualDataAreMissing() {
        CalculoInputDto input = baseInput();
        input.setHashrate(null);
        input.setConsumoW(null);
        input.setPrecioKwh(0.0);
        input.setComision(0.0);
        input.setMoneda("BTC");

        assertThrows(ResponseStatusException.class, () -> service.calcularRentabilidad(input));
    }

    @Test
    void calcularRentabilidadReturnsExpectedValuesWithManualData() {
        MetricasMinado metrics = new MetricasMinado(1, "BTC", "SHA-256", 1000L, 100.0, 10.0, 2.0);
        when(metricasMinadoRepository.findByNombreMonedaIgnoreCase("BTC")).thenReturn(Optional.of(metrics));

        CalculoInputDto input = new CalculoInputDto(
                100.0,
                "BTC",
                0.0,
                0.0,
                0.0,
                null,
                null,
                null,
                null,
                null,
                8640.0);

        CalculoOutputDto output = service.calcularRentabilidad(input);

        assertEquals(1728.0, output.getBeneficioDiario());
        assertEquals(1728.0, output.getBeneficioBrutoDiario());
        assertEquals(51840.0, output.getBeneficioMensual());
        assertEquals(630720.0, output.getBeneficioAnual());
        assertEquals(5.0, output.getRoiDias());
        assertEquals(100.0, output.getHashrate());
        assertEquals(0.0, output.getConsumoW());
        assertEquals(0.0, output.getPrecioKwh());
        assertEquals(0.0, output.getComision());
        assertEquals("SHA-256", output.getAlgoritmoUsado());
    }

    @Test
    void calcularRentabilidadUsesFallbackMetricLookupAndElectricityRepository() {
        MetricasMinado metrics = new MetricasMinado(1, "Bitcoin Cash", "SHA-256", 1000L, 100.0, 10.0, 2.0);
        when(metricasMinadoRepository.findByNombreMonedaIgnoreCase("BitcoinCash")).thenReturn(Optional.empty());
        when(metricasMinadoRepository.findAll()).thenReturn(List.of(metrics));
        when(electricidadPorPaisRepository.findPrecioByPais("Spain")).thenReturn(Optional.of(0.25));

        CalculoInputDto input = new CalculoInputDto(
                100.0,
                "BitcoinCash",
                0.0,
                null,
                null,
                null,
                null,
                "Spain",
                null,
                null,
                8640.0);

        CalculoOutputDto output = service.calcularRentabilidad(input);

        assertEquals(1728.0, output.getBeneficioDiario());
        assertEquals(0.25, output.getPrecioKwh());
        assertEquals(0.0, output.getComision());
    }

    @Test
    void calcularRentabilidadUsesHardwareItemsAndCountryPrice() {
        MetricasMinado metrics = new MetricasMinado(1, "ETH", "Etchash", 1000L, 100.0, 10.0, 2.0);
        Map<String, RendimientoAlgoritmo> algorithms = Map.of(
                "Ethash", new RendimientoAlgoritmo(10.0, 100));
        Gpu gpu = new Gpu("RTX 4090", 450, 1000.0, algorithms);

        when(metricasMinadoRepository.findByNombreMonedaIgnoreCase("ETH")).thenReturn(Optional.of(metrics));
        when(gpuRepository.findByNameIgnoreCase("RTX 4090")).thenReturn(Optional.of(gpu));
        when(electricidadPorPaisRepository.findPrecioByPais("Spain")).thenReturn(Optional.of(0.25));

        CalculoHardwareItemDto gpuItem = new CalculoHardwareItemDto("GPU", "RTX 4090", 1);
        CalculoInputDto input = new CalculoInputDto();
        input.setMoneda("ETH");
        input.setHardwareItems(List.of(gpuItem));
        input.setPais("Spain");
        input.setCostoInicialHardware(8640.0);

        CalculoOutputDto output = service.calcularRentabilidad(input);

        assertEquals("Etchash", output.getAlgoritmoUsado());
        assertEquals(10.0, output.getHashrate());
        assertEquals(100.0, output.getConsumoW());
        assertEquals(0.25, output.getPrecioKwh());
        assertEquals(0.0, output.getComision());
    }

    @Test
    void calcularRentabilidadUsesCpuItemsAndCountryPrice() {
        MetricasMinado metrics = new MetricasMinado(1, "XMR", "RandomX", 1000L, 120.0, 10.0, 2.5);
        Cpu cpu = new Cpu("Ryzen 9", 105, 150);

        when(metricasMinadoRepository.findByNombreMonedaIgnoreCase("XMR")).thenReturn(Optional.of(metrics));
        when(cpuRepository.findByNameIgnoreCase("Ryzen 9")).thenReturn(Optional.of(cpu));
        when(electricidadPorPaisRepository.findPrecioByPais("Spain")).thenReturn(Optional.of(0.25));

        CalculoInputDto input = new CalculoInputDto();
        input.setMoneda("XMR");
        input.setHardwareItems(List.of(new CalculoHardwareItemDto("CPU", "Ryzen 9", 1)));
        input.setPais("Spain");
        input.setCostoInicialHardware(500.0);

        CalculoOutputDto output = service.calcularRentabilidad(input);

        assertEquals("RandomX", output.getAlgoritmoUsado());
        assertEquals(150.0, output.getHashrate());
        assertEquals(105.0, output.getConsumoW());
        assertEquals(0.25, output.getPrecioKwh());
    }

    @Test
    void calcularRentabilidadUsesAsicItemsAndCountryPrice() {
        MetricasMinado metrics = new MetricasMinado(1, "BTC", "SHA-256", 1000L, 120.0, 10.0, 2.5);
        Map<String, RendimientoAlgoritmo> algorithms = Map.of(
                "SHA-256", new RendimientoAlgoritmo(1000.0, 2500));
        Asic asic = new Asic("Antminer S19", 2500, 1000.0, algorithms);

        when(metricasMinadoRepository.findByNombreMonedaIgnoreCase("BTC")).thenReturn(Optional.of(metrics));
        when(asicRepository.findByNameIgnoreCase("Antminer S19")).thenReturn(Optional.of(asic));
        when(electricidadPorPaisRepository.findPrecioByPais("Spain")).thenReturn(Optional.of(0.25));

        CalculoInputDto input = new CalculoInputDto();
        input.setMoneda("BTC");
        input.setHardwareItems(List.of(new CalculoHardwareItemDto("ASIC", "Antminer S19", 1)));
        input.setPais("Spain");
        input.setCostoInicialHardware(5000.0);

        CalculoOutputDto output = service.calcularRentabilidad(input);

        assertEquals("SHA-256", output.getAlgoritmoUsado());
        assertEquals(1000.0, output.getHashrate());
        assertEquals(2500.0, output.getConsumoW());
        assertEquals(0.25, output.getPrecioKwh());
    }

    @Test
    void calcularRentabilidadThrowsWhenPoolIsIncompatibleWithSelectedCoin() {
        MetricasMinado metrics = new MetricasMinado(1, "BTC", "SHA-256", 1000L, 120.0, 10.0, 2.5);
        Pool pool = new Pool("Pool Incompatible", Set.of(Pool.EsquemaDePago.PPS), List.of("EU"), "pool-incompatible");
        pool.setId(1L);

        when(metricasMinadoRepository.findByNombreMonedaIgnoreCase("BTC")).thenReturn(Optional.of(metrics));
        when(poolRepository.findByName("Pool Incompatible")).thenReturn(Optional.of(pool));
        when(poolMonedaComisionRepository.findByPoolIdAndMoneda(1L, "BTC")).thenReturn(Optional.empty());

        CalculoInputDto input = new CalculoInputDto();
        input.setMoneda("BTC");
        input.setHashrate(100.0);
        input.setConsumoW(100.0);
        input.setPrecioKwh(0.10);
        input.setPool("Pool Incompatible");
        input.setCostoInicialHardware(500.0);

        assertThrows(ResponseStatusException.class, () -> service.calcularRentabilidad(input));
    }

    @Test
    void calcularRentabilidadThrowsWhenHardwareAlgorithmIsNotCompatible() {
        MetricasMinado metrics = new MetricasMinado(1, "ETH", "RandomX", 1000L, 100.0, 10.0, 2.0);
        Map<String, RendimientoAlgoritmo> algorithms = Map.of(
                "Ethash", new RendimientoAlgoritmo(10.0, 100));
        Gpu gpu = new Gpu("RTX 4090", 450, 1000.0, algorithms);

        when(metricasMinadoRepository.findByNombreMonedaIgnoreCase("ETH")).thenReturn(Optional.of(metrics));
        when(gpuRepository.findByNameIgnoreCase("RTX 4090")).thenReturn(Optional.of(gpu));

        CalculoInputDto input = new CalculoInputDto();
        input.setMoneda("ETH");
        input.setHardwareItems(List.of(new CalculoHardwareItemDto("GPU", "RTX 4090", 1)));
        input.setPais("Spain");
        input.setCostoInicialHardware(8640.0);

        assertThrows(ResponseStatusException.class, () -> service.calcularRentabilidad(input));
    }

    @Test
    void calcularRentabilidadThrowsWhenCountryPriceIsMissing() {
        MetricasMinado metrics = new MetricasMinado(1, "ETH", "Etchash", 1000L, 100.0, 10.0, 2.0);
        Map<String, RendimientoAlgoritmo> algorithms = Map.of(
                "Ethash", new RendimientoAlgoritmo(10.0, 100));
        Gpu gpu = new Gpu("RTX 4090", 450, 1000.0, algorithms);

        when(metricasMinadoRepository.findByNombreMonedaIgnoreCase("ETH")).thenReturn(Optional.of(metrics));
        when(gpuRepository.findByNameIgnoreCase("RTX 4090")).thenReturn(Optional.of(gpu));
        when(electricidadPorPaisRepository.findPrecioByPais("Nowhere")).thenReturn(Optional.empty());

        CalculoInputDto input = new CalculoInputDto();
        input.setMoneda("ETH");
        input.setHardwareItems(List.of(new CalculoHardwareItemDto("GPU", "RTX 4090", 1)));
        input.setPais("Nowhere");
        input.setCostoInicialHardware(8640.0);

        assertThrows(ResponseStatusException.class, () -> service.calcularRentabilidad(input));
    }

    @Test
    void calcularRentabilidadThrowsWhenMetricasAreMissing() {
        when(metricasMinadoRepository.findByNombreMonedaIgnoreCase("BTC")).thenReturn(Optional.empty());
        when(metricasMinadoRepository.findAll()).thenReturn(List.of());

        CalculoInputDto input = new CalculoInputDto(
                100.0,
                "BTC",
                0.0,
                0.0,
                0.0,
                null,
                null,
                null,
                null,
                null,
                8640.0);

        assertThrows(ResponseStatusException.class, () -> service.calcularRentabilidad(input));
    }

    @Test
    void calcularRentabilidadThrowsWhenMetricPriceIsInvalid() {
        MetricasMinado metrics = new MetricasMinado(1, "BTC", "SHA-256", 1000L, 100.0, 10.0, 0.0);
        when(metricasMinadoRepository.findByNombreMonedaIgnoreCase("BTC")).thenReturn(Optional.of(metrics));

        CalculoInputDto input = new CalculoInputDto(
                100.0,
                "BTC",
                0.0,
                0.0,
                0.0,
                null,
                null,
                null,
                null,
                null,
                8640.0);

        assertThrows(ResponseStatusException.class, () -> service.calcularRentabilidad(input));
    }

    private CalculoInputDto baseManualInput() {
        return new CalculoInputDto(
                100.0,
                "BTC",
                100.0,
                0.10,
                0.0,
                null,
                null,
                "ES",
                null,
                null,
                8640.0);
    }

    private CalculoInputDto baseInput() {
        return new CalculoInputDto();
    }
}