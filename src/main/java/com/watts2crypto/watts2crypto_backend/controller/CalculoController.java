package com.watts2crypto.watts2crypto_backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.watts2crypto.watts2crypto_backend.models.DTOs.CalculoInputDto;
import com.watts2crypto.watts2crypto_backend.models.DTOs.CalculoOutputDto;
import com.watts2crypto.watts2crypto_backend.models.DTOs.MonedaAlgoritmosDto;
import com.watts2crypto.watts2crypto_backend.models.ElectricidadPorPais;
import com.watts2crypto.watts2crypto_backend.service.CalculoService;
import com.watts2crypto.watts2crypto_backend.service.MetricasMinadoService;

@RestController
@RequestMapping("/api/calculo")
public class CalculoController {

	private final CalculoService service;
	private final MetricasMinadoService mmService;

	public CalculoController(CalculoService service, MetricasMinadoService mmService) {
		this.service = service;
		this.mmService = mmService;
	}

	@PostMapping("/rentabilidad")
	public CalculoOutputDto calcularRentabilidad(@RequestBody CalculoInputDto input) {
		return service.calcularRentabilidad(input);
	}

	@GetMapping("/monedasParaCalculo")
	public List<String> fetchAllNombresMonedasDisponibles(){
		return mmService.findAllNombresMonedasParaCalculo();
	}

	@GetMapping("/monedasParaCalculoConAlgoritmo")
	public List<MonedaAlgoritmosDto> fetchAllMonedasDisponiblesConAlgoritmo() {
		return mmService.findAllMonedasParaCalculoConAlgoritmo();
	}

	@GetMapping("/metricas/{moneda}")
	public com.watts2crypto.watts2crypto_backend.models.MetricasMinado fetchMetricasPorMoneda(@PathVariable String moneda) {
		return mmService.findMetricasByNombre(moneda);
	}

	@GetMapping("/paisesElectricidad")
	public List<ElectricidadPorPais> fetchPaisesElectricidad() {
		return service.findPaisesElectricidad();
	}
}
