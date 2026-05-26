package com.watts2crypto.watts2crypto_backend.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.watts2crypto.watts2crypto_backend.service.AsicService;
import com.watts2crypto.watts2crypto_backend.service.CpuService;
import com.watts2crypto.watts2crypto_backend.service.CriptomonedaService;
import com.watts2crypto.watts2crypto_backend.service.ElectricidadService;
import com.watts2crypto.watts2crypto_backend.service.GpuService;
import com.watts2crypto.watts2crypto_backend.service.MetricasMinadoService;
import com.watts2crypto.watts2crypto_backend.service.MaintenanceStateService;
import com.watts2crypto.watts2crypto_backend.service.MonedaTradicionalService;
import com.watts2crypto.watts2crypto_backend.service.PoolService;
import com.watts2crypto.watts2crypto_backend.service.SoftwareService;

@RestController
@RequestMapping("/api/refresh")
public class RefreshController {

	private final MonedaTradicionalService monedaTradicionalService;
	private final CriptomonedaService criptomonedaService;
	private final GpuService gpuService;
	private final AsicService asicService;
	private final CpuService cpuService;
	private final MetricasMinadoService metricaMinadoService;
	private final PoolService poolService;
	private final SoftwareService softwareService;
	private final ElectricidadService electricidadService;
	private final MaintenanceStateService maintenanceStateService;

	@Value("${refresh.token:}")
	private String refreshDataToken;

	public RefreshController(MonedaTradicionalService monedaTradicionalService,
			CriptomonedaService criptomonedaService,
			GpuService gpuService,
			AsicService asicService,
			CpuService cpuService,
			MetricasMinadoService metricaMinadoService,
			PoolService poolService,
			SoftwareService softwareService,
			ElectricidadService electricidadService,
			MaintenanceStateService maintenanceStateService) {
		this.monedaTradicionalService = monedaTradicionalService;
		this.criptomonedaService = criptomonedaService;
		this.gpuService = gpuService;
		this.asicService = asicService;
		this.cpuService = cpuService;
		this.metricaMinadoService = metricaMinadoService;
		this.poolService = poolService;
		this.softwareService = softwareService;
		this.electricidadService = electricidadService;
		this.maintenanceStateService = maintenanceStateService;
	}

	@PostMapping("/fiat")
	public ResponseEntity<Map<String, String>> refreshFiat(
			@RequestHeader(value = "X-Refresh-Token", required = false) String refreshToken,
			@RequestHeader(value = "Authorization", required = false) String authorization) {
		validarToken(refreshToken, authorization);
		return ejecutarRefresco("refresh-fiat", monedaTradicionalService::refreshMonedasTradicionales);
	}

	@PostMapping("/fiat/symbols")
	public ResponseEntity<Map<String, String>> refreshFiatSymbols(
			@RequestHeader(value = "X-Refresh-Token", required = false) String refreshToken,
			@RequestHeader(value = "Authorization", required = false) String authorization) {
		validarToken(refreshToken, authorization);
		return ejecutarRefresco("refresh-fiat-symbols", monedaTradicionalService::refreshSymbols);
	}

	@PostMapping("/criptomonedas")
	public ResponseEntity<Map<String, String>> refreshCriptomonedas(
			@RequestHeader(value = "X-Refresh-Token", required = false) String refreshToken,
			@RequestHeader(value = "Authorization", required = false) String authorization) {
		validarToken(refreshToken, authorization);
		return ejecutarRefresco("refresh-criptomonedas", criptomonedaService::refreshCriptomonedas);
	}

	@PostMapping("/gpus")
	public ResponseEntity<Map<String, String>> refreshGpus(
			@RequestHeader(value = "X-Refresh-Token", required = false) String refreshToken,
			@RequestHeader(value = "Authorization", required = false) String authorization) {
		validarToken(refreshToken, authorization);
		return ejecutarRefresco("refresh-gpus", gpuService::refreshGpus);
	}

	@PostMapping("/asics")
	public ResponseEntity<Map<String, String>> refreshAsics(
			@RequestHeader(value = "X-Refresh-Token", required = false) String refreshToken,
			@RequestHeader(value = "Authorization", required = false) String authorization) {
		validarToken(refreshToken, authorization);
		return ejecutarRefresco("refresh-asics", asicService::refreshAsics);
	}

	@PostMapping("/cpus")
	public ResponseEntity<Map<String, String>> refreshCpus(
			@RequestHeader(value = "X-Refresh-Token", required = false) String refreshToken,
			@RequestHeader(value = "Authorization", required = false) String authorization) {
		validarToken(refreshToken, authorization);
		return ejecutarRefresco("refresh-cpus", cpuService::refreshCpus);
	}

	@PostMapping("/metricas-minado")
	public ResponseEntity<Map<String, String>> refreshMetricasMinado(
			@RequestHeader(value = "X-Refresh-Token", required = false) String refreshToken,
			@RequestHeader(value = "Authorization", required = false) String authorization) {
		validarToken(refreshToken, authorization);
		return ejecutarRefresco("refresh-metricas-minado", metricaMinadoService::refreshMetricasMinado);
	}

	@PostMapping("/pools")
	public ResponseEntity<Map<String, String>> refreshPools(
			@RequestHeader(value = "X-Refresh-Token", required = false) String refreshToken,
			@RequestHeader(value = "Authorization", required = false) String authorization) {
		validarToken(refreshToken, authorization);
		return ejecutarRefresco("refresh-pools", poolService::refreshPools);
	}

	@PostMapping("/software")
	public ResponseEntity<Map<String, String>> refreshSoftware(
			@RequestHeader(value = "X-Refresh-Token", required = false) String refreshToken,
			@RequestHeader(value = "Authorization", required = false) String authorization) {
		validarToken(refreshToken, authorization);
		return ejecutarRefresco("refresh-software", softwareService::refreshSoftware);
	}

	@PostMapping("/electricidad")
	public ResponseEntity<Map<String, String>> refreshElectricidad(
			@RequestHeader(value = "X-Refresh-Token", required = false) String refreshToken,
			@RequestHeader(value = "Authorization", required = false) String authorization) {
		validarToken(refreshToken, authorization);
		return ejecutarRefresco("refresh-electricidad", electricidadService::refreshElectricidad);
	}

	private ResponseEntity<Map<String, String>> ejecutarRefresco(String operation, Runnable action) {
		maintenanceStateService.beginRefresh(operation);
		try {
			action.run();
			return ResponseEntity.ok(Map.of("status", "ok", "operation", operation));
		} finally {
			maintenanceStateService.endRefresh(operation);
		}
	}

	private void validarToken(String refreshToken, String authorization) {
		if (!StringUtils.hasText(refreshDataToken)) {
			throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
					"El token de refresco no está configurado en el backend.");
		}

		String tokenProporcionado = null;
		if (StringUtils.hasText(refreshToken)) {
			tokenProporcionado = refreshToken.trim();
		} else if (StringUtils.hasText(authorization)) {
			String valor = authorization.trim();
			if (valor.regionMatches(true, 0, "Bearer ", 0, 7)) {
				tokenProporcionado = valor.substring(7).trim();
			} else {
				tokenProporcionado = valor;
			}
		}

		if (!refreshDataToken.equals(tokenProporcionado)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"No autorizado para ejecutar este refresco.");
		}
	}
}
