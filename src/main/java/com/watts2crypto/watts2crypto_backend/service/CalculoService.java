package com.watts2crypto.watts2crypto_backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.watts2crypto.watts2crypto_backend.models.Asic;
import com.watts2crypto.watts2crypto_backend.models.Criptomoneda;
import com.watts2crypto.watts2crypto_backend.models.CriptomonedaPrecio;
import com.watts2crypto.watts2crypto_backend.models.Cpu;
import com.watts2crypto.watts2crypto_backend.models.DTOs.CalculoInputDto;
import com.watts2crypto.watts2crypto_backend.models.DTOs.CalculoOutputDto;
import com.watts2crypto.watts2crypto_backend.models.Gpu;
import com.watts2crypto.watts2crypto_backend.models.Pool;
import com.watts2crypto.watts2crypto_backend.models.MetricasMinado;
import com.watts2crypto.watts2crypto_backend.models.RendimientoAlgoritmo;
import com.watts2crypto.watts2crypto_backend.models.Software;
import com.watts2crypto.watts2crypto_backend.repositories.AsicRepository;
import com.watts2crypto.watts2crypto_backend.repositories.CpuRepository;
import com.watts2crypto.watts2crypto_backend.repositories.ElectricidadPorPaisRepository;
import com.watts2crypto.watts2crypto_backend.repositories.GpuRepository;
import com.watts2crypto.watts2crypto_backend.repositories.MetricasMinadoRepository;
import com.watts2crypto.watts2crypto_backend.repositories.PoolRepository;
import com.watts2crypto.watts2crypto_backend.repositories.SoftwareRepository;

@Service
public class CalculoService {

    private final CriptomonedaService criptomonedaService;
    private final CpuRepository cpuRepository;
    private final GpuRepository gpuRepository;
    private final AsicRepository asicRepository;
    private final ElectricidadPorPaisRepository electricidadPorPaisRepository;
    private final PoolRepository poolRepository;
    private final SoftwareRepository softwareRepository;
    private final MetricasMinadoRepository metricasMinadoRepository;

    public CalculoService(CriptomonedaService criptomonedaService,
            CpuRepository cpuRepository,
            GpuRepository gpuRepository,
            AsicRepository asicRepository,
            ElectricidadPorPaisRepository electricidadPorPaisRepository,
            PoolRepository poolRepository,
            SoftwareRepository softwareRepository,
            MetricasMinadoRepository metricasMinadoRepository) {
        this.criptomonedaService = criptomonedaService;
        this.cpuRepository = cpuRepository;
        this.gpuRepository = gpuRepository;
        this.asicRepository = asicRepository;
        this.electricidadPorPaisRepository = electricidadPorPaisRepository;
        this.poolRepository = poolRepository;
        this.softwareRepository = softwareRepository;
        this.metricasMinadoRepository = metricasMinadoRepository;
    }

    public CalculoOutputDto calcularRentabilidad(CalculoInputDto input) {
        try {
            validarInput(input);

            double hashrate = resolverHashrate(input);
            double consumoW = resolverConsumoW(input);
            double precioKwh = resolverPrecioKwh(input);
            double comision = resolverComision(input);

                MetricasMinado coinStats = findMetricasMinado(input);
            double monedasMinadasPorDia = calcularMonedasMinadasDia(
                    hashrate,
                    coinStats.getNethash().doubleValue(),
                    coinStats.getBlockTimeSegundos(),
                    coinStats.getBlockReward());

            Criptomoneda cripto = resolverCriptomoneda(input.getMoneda());
            double precioEur = obtenerPrecioEurMasReciente(cripto);

            double ingresoBrutoDiario = monedasMinadasPorDia * precioEur;
            double ingresoNetoDiario = ingresoBrutoDiario * (1.0 - (comision / 100.0));

            double costeEnergiaDiario = ((consumoW / 1000.0) * 24.0) * precioKwh;
            double beneficioDiario = redondear2(ingresoNetoDiario - costeEnergiaDiario);
            double beneficioMensual = redondear2(beneficioDiario * 30.0);
            double beneficioAnual = redondear2(beneficioDiario * 365.0);
            Double roiDias = calcularRoiDias(input.getCostoInicialHardware(), beneficioDiario);

            return new CalculoOutputDto(beneficioDiario, beneficioMensual, beneficioAnual, roiDias);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public List<String> findAlgoritmosSoportadosPorHardware(String tipoHardware, String nombreHardware) {
        if (tipoHardware == null || tipoHardware.isBlank() || nombreHardware == null || nombreHardware.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Debes indicar algún elemento de hadrware.");
        }

        String tipo = tipoHardware.trim().toUpperCase(Locale.ROOT);
        String nombre = nombreHardware.trim();

        switch (tipo) {
            case "CPU":
                Cpu cpu = cpuRepository.findByNameIgnoreCase(nombre)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "CPU no encontrada en la base de datos."));
                if (cpu.getHashrate() == null || cpu.getConsumoNominal() == null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "La CPU seleccionada no tiene datos suficientes para calcular.");
                }
                return List.of("RandomX");

            case "GPU":
                Gpu gpu = gpuRepository.findByNameIgnoreCase(nombre)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "GPU no encontrada en la base de datos."));
                return extraerAlgoritmosOrdenados(gpu.getAlgorithms());

            case "ASIC":
                Asic asic = asicRepository.findByNameIgnoreCase(nombre)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "ASIC no encontrado en la base de datos."));
                return extraerAlgoritmosOrdenados(asic.getAlgorithms());

            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Tipo de hardware invalido. Valores permitidos: CPU, GPU, ASIC.");
        }
    }

    private void validarInput(CalculoInputDto input) {
        if (input == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El input del calculo no puede ser null.");
        }
        if (input.getMoneda() == null || input.getMoneda().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Se debe indicar la moneda a minar.");
        }
        if (input.getHashrate() != null && input.getHashrate() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El hashrate debe ser mayor que 0.");
        }
        if (input.getHashrate() == null && !hayDatosHardware(input)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Debes indicar el hashrate manualmente o seleccionar hardware.");
        }
        if (input.getConsumoW() != null && input.getConsumoW() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El consumo debe ser mayor o igual que 0.");
        }
        if (input.getConsumoW() == null && !hayDatosHardware(input)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Debes indicar el consumo manualmente o seleccionar hardware.");
        }
        if (input.getPrecioKwh() != null && input.getPrecioKwh() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El precio de la luz debe ser mayor o igual que 0.");
        }
        if (input.getPrecioKwh() == null && (input.getPais() == null || input.getPais().isBlank())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Debes indicar el precio de la luz manualmente o seleccionar un pais.");
        }
        if (input.getComision() != null && (input.getComision() < 0 || input.getComision() > 100)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La comision debe estar entre 0 y 100.");
        }
        if (input.getCostoInicialHardware() != null && input.getCostoInicialHardware() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El costo inicial de hardware debe ser mayor que 0.");
        }
    }

    private double resolverHashrate(CalculoInputDto input) {
        if (input.getHashrate() != null) {
            return input.getHashrate();
        }
        return obtenerRendimientoDesdeHardware(input).hashrate;
    }

    private double resolverConsumoW(CalculoInputDto input) {
        if (input.getConsumoW() != null) {
            return input.getConsumoW();
        }
        return obtenerRendimientoDesdeHardware(input).consumoW;
    }

    private double resolverPrecioKwh(CalculoInputDto input) {
        if (input.getPrecioKwh() != null) {
            return input.getPrecioKwh();
        }

        String pais = input.getPais().trim();
        return electricidadPorPaisRepository.findPrecioByPais(pais)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No se encontro precio de electricidad para el pais indicado."));
    }

    private double resolverComision(CalculoInputDto input) {
        if (input.getComision() != null) {
            return input.getComision();
        }

        double comisionTotal = 0.0;
        if (input.getPool() != null && !input.getPool().isBlank()) {
            Pool pool = poolRepository.findByName(input.getPool().trim())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Pool no encontrada en la base de datos."));
            comisionTotal += pool.getComision();
        }
        if (input.getSoftware() != null && !input.getSoftware().isBlank()) {
            Software software = softwareRepository.findByName(input.getSoftware().trim())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Software de minado no encontrado en la base de datos."));
            comisionTotal += software.getComision();
        }

        if (comisionTotal > 100.0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La comision total de pool/software no puede superar 100.");
        }

        return comisionTotal;
    }

    private RendimientoHardware obtenerRendimientoDesdeHardware(CalculoInputDto input) {
        if (!hayDatosHardware(input)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Faltan datos de hardware para resolver hashrate/consumo desde BD.");
        }

        String tipo = input.getTipoHardware().trim().toUpperCase(Locale.ROOT);
        String nombre = input.getNombreHardware().trim();

        switch (tipo) {
            case "CPU":
                Cpu cpu = cpuRepository.findByNameIgnoreCase(nombre)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "CPU no encontrada en la base de datos."));
                if (cpu.getHashrate() == null || cpu.getConsumoNominal() == null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "La CPU seleccionada no tiene hashrate/consumo disponible.");
                }
                return new RendimientoHardware(cpu.getHashrate().doubleValue(), cpu.getConsumoNominal().doubleValue());

            case "GPU":
                String algoritmoGpu = validarYNormalizarAlgoritmo(input.getAlgoritmo());
                Gpu gpu = gpuRepository.findByNameIgnoreCase(nombre)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "GPU no encontrada en la base de datos."));
                Double hashrateGpu = gpu.getSpeedHashesPorSegundo(algoritmoGpu);
                Double consumoGpu = gpu.getConsumoEnWatts(algoritmoGpu);
                if (hashrateGpu == null || consumoGpu == null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "La GPU seleccionada no tiene datos para el algoritmo indicado.");
                }
                return new RendimientoHardware(hashrateGpu, consumoGpu);

            case "ASIC":
                String algoritmoAsic = validarYNormalizarAlgoritmo(input.getAlgoritmo());
                Asic asic = asicRepository.findByNameIgnoreCase(nombre)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "ASIC no encontrado en la base de datos."));
                Double hashrateAsic = asic.getSpeedHashesPorSegundo(algoritmoAsic);
                Double consumoAsic = asic.getConsumoEnWatts(algoritmoAsic);
                if (hashrateAsic == null || consumoAsic == null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "El ASIC seleccionado no tiene datos para el algoritmo indicado.");
                }
                return new RendimientoHardware(hashrateAsic, consumoAsic);

            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "tipoHardware invalido. Valores permitidos: CPU, GPU, ASIC.");
        }
    }

    private String validarYNormalizarAlgoritmo(String algoritmo) {
        if (algoritmo == null || algoritmo.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Debes indicar algoritmo cuando se usa hardware GPU o ASIC.");
        }
        return algoritmo.trim();
    }

    private boolean hayDatosHardware(CalculoInputDto input) {
        return input.getTipoHardware() != null
                && !input.getTipoHardware().isBlank()
                && input.getNombreHardware() != null
                && !input.getNombreHardware().isBlank();
    }

    private Double calcularRoiDias(Double costoInicialHardware, double beneficioDiario) {
        if (costoInicialHardware == null || beneficioDiario <= 0) {
            return null;
        }
        return redondear2(costoInicialHardware / beneficioDiario);
    }

    private MetricasMinado findMetricasMinado(CalculoInputDto input) {
        if (input.getMoneda() != null && !input.getMoneda().isBlank()) {
            return metricasMinadoRepository.findByNombreMonedaIgnoreCase(input.getMoneda())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "No se encontraron métricas de minado para la moneda seleccionada."));
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
            "Debes indicar la moneda para obtener métricas de minado.");
    }

    private Criptomoneda resolverCriptomoneda(String moneda) {
        String normalizada = moneda.trim().toLowerCase();
        try {
            return criptomonedaService.findCriptomonedaPorAssetId(normalizada);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private double obtenerPrecioEurMasReciente(Criptomoneda cripto) {
        List<CriptomonedaPrecio> historico = cripto.getHistoricoPrecios();
        if (historico == null || historico.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No hay historico disponible para la criptomoneda seleccionada.");
        }

        return historico.stream()
                .map(CriptomonedaPrecio::getPrecioEur)
                .filter(p -> p != null)
                .reduce((first, second) -> second)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No hay precio disponible para la criptomoneda seleccionada."))
                .doubleValue();
    }

    private double redondear2(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

    private List<String> extraerAlgoritmosOrdenados(Map<String, RendimientoAlgoritmo> algoritmos) {
        if (algoritmos == null || algoritmos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No hay algoritmos soportados para el hardware seleccionado.");
        }
        List<String> nombres = new ArrayList<>(algoritmos.keySet());
        nombres.sort(String.CASE_INSENSITIVE_ORDER);
        return nombres;
    }

    private double calcularMonedasMinadasDia(double hashrateMinero, double nethash, double blockTimeSegundos,
            double blockReward) {
        double bloquesPorDia = 86400.0 / blockTimeSegundos;
        double participacionRed = hashrateMinero / nethash;
        return participacionRed * bloquesPorDia * blockReward;
    }

    private static class RendimientoHardware {
        private final double hashrate;
        private final double consumoW;

        private RendimientoHardware(double hashrate, double consumoW) {
            this.hashrate = hashrate;
            this.consumoW = consumoW;
        }
    }
}
