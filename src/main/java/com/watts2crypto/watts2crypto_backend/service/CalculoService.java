package com.watts2crypto.watts2crypto_backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.watts2crypto.watts2crypto_backend.models.Asic;
import com.watts2crypto.watts2crypto_backend.models.Cpu;
import com.watts2crypto.watts2crypto_backend.models.DTOs.CalculoHardwareItemDto;
import com.watts2crypto.watts2crypto_backend.models.DTOs.CalculoInputDto;
import com.watts2crypto.watts2crypto_backend.models.DTOs.CalculoOutputDto;
import com.watts2crypto.watts2crypto_backend.models.Gpu;
import com.watts2crypto.watts2crypto_backend.models.ElectricidadPorPais;
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
import com.watts2crypto.watts2crypto_backend.repositories.PoolMonedaComisionRepository;
import com.watts2crypto.watts2crypto_backend.repositories.SoftwareAlgoritmoMonedaRepository;

@Service
public class CalculoService {

    private final CpuRepository cpuRepository;
    private final GpuRepository gpuRepository;
    private final AsicRepository asicRepository;
    private final ElectricidadPorPaisRepository electricidadPorPaisRepository;
    private final PoolRepository poolRepository;
    private final SoftwareRepository softwareRepository;
    private final MetricasMinadoRepository metricasMinadoRepository;
    private final SoftwareAlgoritmoMonedaRepository softwareAlgoritmoMonedaRepository;
    private final PoolMonedaComisionRepository poolMonedaComisionRepository;

    public CalculoService(CpuRepository cpuRepository,
            GpuRepository gpuRepository,
            AsicRepository asicRepository,
            ElectricidadPorPaisRepository electricidadPorPaisRepository,
            PoolRepository poolRepository,
            SoftwareRepository softwareRepository,
            MetricasMinadoRepository metricasMinadoRepository,
            SoftwareAlgoritmoMonedaRepository softwareAlgoritmoMonedaRepository,
            PoolMonedaComisionRepository poolMonedaComisionRepository) {
        this.cpuRepository = cpuRepository;
        this.gpuRepository = gpuRepository;
        this.asicRepository = asicRepository;
        this.electricidadPorPaisRepository = electricidadPorPaisRepository;
        this.poolRepository = poolRepository;
        this.softwareRepository = softwareRepository;
        this.metricasMinadoRepository = metricasMinadoRepository;
        this.softwareAlgoritmoMonedaRepository = softwareAlgoritmoMonedaRepository;
        this.poolMonedaComisionRepository = poolMonedaComisionRepository;
    }

    public CalculoOutputDto calcularRentabilidad(CalculoInputDto input) {
        try {
            validarInput(input);
            MetricasMinado coinStats = findMetricasMinado(input);
            validarCompatibilidadMonedaAlgoritmoYSoftware(input, coinStats);
            String algoritmo = coinStats.getAlgoritmo();

            double hashrate = resolverHashrate(input, algoritmo);
            double consumoW = resolverConsumoW(input, algoritmo);
            double precioKwh = resolverPrecioKwh(input);
            double comision = resolverComision(input);

            double monedasMinadasPorDia = calcularMonedasMinadasDia(
                    hashrate,
                    coinStats.getNethash().doubleValue(),
                    coinStats.getBlockTimeSegundos(),
                    coinStats.getBlockReward());

                double precioEur = coinStats.getPrecioActualEur();
                if (precioEur <= 0.0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "La moneda indicada no tiene un precio actual válido para el cálculo.");
                }

            double ingresoBrutoDiario = monedasMinadasPorDia * precioEur;
            double ingresoNetoDiario = ingresoBrutoDiario * (1.0 - (comision / 100.0));
            double costeEnergiaDiario = ((consumoW / 1000.0) * 24.0) * precioKwh;

            double beneficioDiario = redondear2(ingresoNetoDiario - costeEnergiaDiario);
            double beneficioMensual = redondear2(beneficioDiario * 30.0);
            double beneficioAnual = redondear2(beneficioDiario * 365.0);
            Double roiDias = calcularRoiDias(input.getCostoInicialHardware(), beneficioDiario);

            return new CalculoOutputDto(
                    beneficioDiario,
                    redondear2(ingresoBrutoDiario),
                    beneficioMensual,
                    beneficioAnual,
                    roiDias,
                    redondear2(hashrate),
                    redondear2(consumoW),
                    redondear2(precioKwh),
                    redondear2(comision),
                    redondear2(costeEnergiaDiario),
                    algoritmo);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public List<ElectricidadPorPais> findPaisesElectricidad() {
        return electricidadPorPaisRepository.findAllPaises();
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

        if (input.getHardwareItems() != null) {
            for (CalculoHardwareItemDto item : input.getHardwareItems()) {
                if (item == null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Los elementos de hardware no pueden ser nulos.");
                }
                if (item.getTipoHardware() == null || item.getTipoHardware().isBlank()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Cada elemento de hardware debe indicar tipoHardware.");
                }
                if (item.getNombreHardware() == null || item.getNombreHardware().isBlank()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Cada elemento de hardware debe indicar nombreHardware.");
                }
                if (item.getCantidad() != null && item.getCantidad() <= 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "La cantidad de cada elemento de hardware debe ser mayor que 0.");
                }
            }
        }

        if (input.getMoneda() == null || input.getMoneda().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Se debe indicar la moneda a minar.");
        }
        if (input.getHashrate() != null && input.getHashrate() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El hashrate debe ser mayor que 0.");
        }
        if (input.getHashrate() == null && !hayDatosHardware(input)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Debes indicar el hashrate manualmente o seleccionar hardware de minería.");
        }
        if (input.getConsumoW() != null && input.getConsumoW() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El consumo debe ser mayor o igual que 0.");
        }
        if (input.getConsumoW() == null && !hayDatosHardware(input)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Debes indicar el consumo manualmente o seleccionar hardware de minería.");
        }
        if (input.getPrecioKwh() != null && input.getPrecioKwh() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El precio de la luz debe ser mayor o igual que 0.");
        }
        if (input.getPrecioKwh() == null && (input.getPais() == null || input.getPais().isBlank())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Debes indicar el precio de la luz manualmente o seleccionar un país.");
        }
        if (input.getComision() != null && (input.getComision() < 0 || input.getComision() > 100)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La comisión debe estar entre 0 y 100.");
        }
        if (input.getCostoInicialHardware() != null && input.getCostoInicialHardware() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El costo inicial de hardware no puede ser negativo.");
        }
    }

    private double resolverHashrate(CalculoInputDto input, String algoritmo) {
        if (input.getHashrate() != null) {
            return input.getHashrate();
        }
        return obtenerRendimientoDesdeHardware(input, algoritmo).hashrate;
    }

    private double resolverConsumoW(CalculoInputDto input, String algoritmo) {
        if (input.getConsumoW() != null) {
            return input.getConsumoW();
        }
        return obtenerRendimientoDesdeHardware(input, algoritmo).consumoW;
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

        String moneda = input.getMoneda().trim();
        double comisionTotal = 0.0;
        if (input.getPool() != null && !input.getPool().isBlank()) {
            Pool pool = poolRepository.findByName(input.getPool().trim())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Pool no encontrada en la base de datos."));
            comisionTotal += poolMonedaComisionRepository
                .findComisionByPoolIdAndMoneda(pool.getId(), moneda)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La pool seleccionada no da soporte para la moneda " + moneda + "."));
        }
        if (input.getSoftware() != null && !input.getSoftware().isBlank()) {
            Software software = softwareRepository.findByName(input.getSoftware().trim())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Software de minado no encontrado en la base de datos."));
            comisionTotal += softwareAlgoritmoMonedaRepository
                .findComisionBySoftwareIdAndMoneda(software.getId(), moneda)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El software seleccionado no da soporte para la moneda " + moneda + "."));
        }

        if (comisionTotal > 100.0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La comision total de pool/software no puede superar 100.");
        }

        return comisionTotal;
    }

    private RendimientoHardware obtenerRendimientoDesdeHardware(CalculoInputDto input, String algoritmo) {
        List<CalculoHardwareItemDto> hardwareItems = obtenerHardwareItemsNormalizados(input);
        if (hardwareItems.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Faltan datos de hardware para resolver hashrate/consumo desde BD.");
        }

        double hashrateTotal = 0.0;
        double consumoTotal = 0.0;

        for (CalculoHardwareItemDto item : hardwareItems) {
            RendimientoHardware rendimientoUnitario = obtenerRendimientoUnitario(item, algoritmo);
            int cantidad = item.getCantidad() == null ? 1 : item.getCantidad();

            hashrateTotal += rendimientoUnitario.hashrate * cantidad;
            consumoTotal += rendimientoUnitario.consumoW * cantidad;
        }

        return new RendimientoHardware(hashrateTotal, consumoTotal);
    }

    private RendimientoHardware obtenerRendimientoUnitario(CalculoHardwareItemDto item, String algoritmoInput) {
        String tipo = item.getTipoHardware().trim().toUpperCase(Locale.ROOT);
        String nombre = item.getNombreHardware().trim();

        switch (tipo) {
            case "CPU":
            String algoritmoCpu = validarYNormalizarAlgoritmo(algoritmoInput);
            if (!normalizarTextoComparacion(algoritmoCpu).equals(normalizarTextoComparacion("RandomX"))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Las CPUs solo son compatibles con el algoritmo RandomX, el cual no es el que usa la moneda indicada.");
            }

                Cpu cpu = cpuRepository.findByNameIgnoreCase(nombre)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "CPU no encontrada en la base de datos."));
                if (cpu.getHashrate() == null || cpu.getConsumoNominal() == null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "La CPU seleccionada no tiene hashrate/consumo disponible.");
                }
                return new RendimientoHardware(cpu.getHashrate().doubleValue(), cpu.getConsumoNominal().doubleValue());

            case "GPU":
                String algoritmoGpu = validarYNormalizarAlgoritmo(algoritmoInput);
                Gpu gpu = gpuRepository.findByNameIgnoreCase(nombre)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "GPU no encontrada en la base de datos."));
                String algoritmoGpuResuelto = resolverAlgoritmoCompatible(gpu.getAlgorithms(), algoritmoGpu);
                Double hashrateGpu = algoritmoGpuResuelto == null ? null : gpu.getSpeedHashesPorSegundo(algoritmoGpuResuelto);
                Integer consumoGpu = algoritmoGpuResuelto == null ? null : gpu.getConsumoEnWatts(algoritmoGpuResuelto);
                if (hashrateGpu == null || consumoGpu == null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "La GPU seleccionada no tiene datos para el algoritmo " + algoritmoInput + ".");
                }
                return new RendimientoHardware(hashrateGpu, consumoGpu);

            case "ASIC":
                String algoritmoAsic = validarYNormalizarAlgoritmo(algoritmoInput);
                Asic asic = asicRepository.findByNameIgnoreCase(nombre)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "ASIC no encontrado en la base de datos."));
                String algoritmoAsicResuelto = resolverAlgoritmoCompatible(asic.getAlgorithms(), algoritmoAsic);
                Double hashrateAsic = algoritmoAsicResuelto == null ? null : asic.getSpeedHashesPorSegundo(algoritmoAsicResuelto);
                Integer consumoAsic = algoritmoAsicResuelto == null ? null : asic.getConsumoEnWatts(algoritmoAsicResuelto);
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

    private List<CalculoHardwareItemDto> obtenerHardwareItemsNormalizados(CalculoInputDto input) {
        List<CalculoHardwareItemDto> items = input.getHardwareItems();
        if (items != null && !items.isEmpty()) {
            return items;
        }

        return List.of();
    }

    private String validarYNormalizarAlgoritmo(String algoritmo) {
        if (algoritmo == null || algoritmo.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Debes indicar algoritmo cuando se usa hardware GPU o ASIC.");
        }
        return algoritmo.trim();
    }

    private boolean hayDatosHardware(CalculoInputDto input) {
        return input.getHardwareItems() != null && !input.getHardwareItems().isEmpty();
    }

    private Double calcularRoiDias(Double costoInicialHardware, double beneficioDiario) {
        if (costoInicialHardware == null || beneficioDiario <= 0) {
            return null;
        }
        return redondear2(costoInicialHardware / beneficioDiario);
    }

    private MetricasMinado findMetricasMinado(CalculoInputDto input) {
        if (input.getMoneda() != null && !input.getMoneda().isBlank()) {
            String monedaInput = input.getMoneda().trim();

            return metricasMinadoRepository.findByNombreMonedaIgnoreCase(monedaInput)
                    .or(() -> metricasMinadoRepository.findAll().stream()
                            .filter(mm -> normalizarNombreMoneda(mm.getNombreMoneda())
                                    .equals(normalizarNombreMoneda(monedaInput)))
                            .findFirst())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "No se encontraron métricas de minado para la moneda indicada."));
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Debes indicar la moneda para obtener métricas de minado.");
    }

    private String normalizarNombreMoneda(String valor) {
        if (valor == null) {
            return "";
        }
        return normalizarTextoComparacion(valor);
    }

    private void validarCompatibilidadMonedaAlgoritmoYSoftware(CalculoInputDto input, MetricasMinado metricasMoneda) {
        String algoritmoMoneda = metricasMoneda.getAlgoritmo();
        String moneda = input.getMoneda().trim();
        if (algoritmoMoneda == null || algoritmoMoneda.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La moneda seleccionada no se puede minar con el algoritmo indicado.");
        }

        if (input.getPool() != null && !input.getPool().isBlank()) {
            Pool pool = poolRepository.findByName(input.getPool().trim())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Pool no encontrada en la base de datos."));

            boolean poolCompatible = poolMonedaComisionRepository
                    .findByPoolIdAndMoneda(pool.getId(), moneda)
                    .isPresent();

            if (!poolCompatible) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "La pool indicada no tiene soporte para la moneda seleccionada (" + moneda + ").");
            }
        }

        if (input.getSoftware() != null && !input.getSoftware().isBlank()) {
            Software software = softwareRepository.findByName(input.getSoftware().trim())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Software de minado no encontrado en la base de datos."));

            var detalleSoftware = softwareAlgoritmoMonedaRepository
                    .findBySoftwareIdAndMoneda(software.getId(), moneda)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "El software indicado no tiene soporte para la moneda seleccionada (" + moneda + ")."));

            String algoritmoMonedaNormalizado = normalizarTextoComparacion(algoritmoMoneda);
            String algoritmoSoftware = detalleSoftware.getAlgoritmo();
            boolean softwareCompatible = algoritmoSoftware != null
                    && !algoritmoSoftware.isBlank()
                    && sonAlgoritmosCompatibles(
                            normalizarTextoComparacion(algoritmoSoftware),
                            algoritmoMonedaNormalizado);

            if (!softwareCompatible) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "El software indicado no soporta el algoritmo " + algoritmoMoneda
                                + ", requerido por la moneda seleccionada.");
            }
        }
    }

    private String normalizarTextoComparacion(String valor) {
        return valor.trim()
                .toLowerCase(Locale.ROOT)
                .replace(" ", "")
                .replace("-", "")
                .replace("_", "");
    }

    private String resolverAlgoritmoCompatible(Map<String, RendimientoAlgoritmo> algoritmosDisponibles, String algoritmoSolicitado) {
        if (algoritmosDisponibles == null || algoritmosDisponibles.isEmpty() || algoritmoSolicitado == null) {
            return null;
        }

        if (algoritmosDisponibles.containsKey(algoritmoSolicitado)) {
            return algoritmoSolicitado;
        }

        String solicitadoNormalizado = normalizarTextoComparacion(algoritmoSolicitado);
        for (String algoritmoDisponible : algoritmosDisponibles.keySet()) {
            if (algoritmoDisponible == null || algoritmoDisponible.isBlank()) {
                continue;
            }
            String disponibleNormalizado = normalizarTextoComparacion(algoritmoDisponible);
            if (solicitadoNormalizado.equals(disponibleNormalizado)
                    || sonAlgoritmosCompatibles(disponibleNormalizado, solicitadoNormalizado)) {
                return algoritmoDisponible;
            }
        }

        return null;
    }

    private boolean sonAlgoritmosCompatibles(String algoritmoA, String algoritmoB) {
        return normalizarFamiliaAlgoritmo(algoritmoA).equals(normalizarFamiliaAlgoritmo(algoritmoB));
    }

    private String normalizarFamiliaAlgoritmo(String algoritmo) {
        String normalizado = normalizarTextoComparacion(algoritmo);
        if (normalizado.equals("ethash")
                || normalizado.equals("etchash")
                || normalizado.equals("ethash4g")
                || normalizado.equals("ethashb3")) {
            return "ethashfamily";
        }
        return normalizado;
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
