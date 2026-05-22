package com.watts2crypto.watts2crypto_backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.watts2crypto.watts2crypto_backend.models.Gpu;
import com.watts2crypto.watts2crypto_backend.models.RendimientoAlgoritmo;
import com.watts2crypto.watts2crypto_backend.service.GpuService;

@RestController
@RequestMapping("/api/gpus")
public class GpuController {

	private final GpuService service;

	public GpuController(GpuService service) {
		this.service = service;
	}

	@GetMapping
	public List<Gpu> getAllGpus() {
		return service.findAllGpus();
	}

	@GetMapping("/{nombre}")
	public Gpu getGpuByName(@PathVariable String nombre) {
		return service.findGpuByName(nombre);
	}

	@GetMapping("/nombres")
	public List<String> getAllGpuNames() {
		return service.findAllGpuNames();
	}

	@GetMapping("/{nombre}/{algoritmo}")
	public RendimientoAlgoritmo getHashrateYConsumoPorNombreYAlgoritmo(@PathVariable String nombre,
																		@PathVariable String algoritmo) {
		return service.findHashrateYConsumoPorNombreYAlgoritmo(nombre, algoritmo);
	}
	@GetMapping("/byAlgoritmo/{algoritmo}")
	public List<String> getGpusByAlgorithm(@PathVariable String algoritmo) {
		return service.findNamesByAlgorithm(algoritmo);
	}}