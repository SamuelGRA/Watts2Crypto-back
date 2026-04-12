package com.watts2crypto.watts2crypto_backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.watts2crypto.watts2crypto_backend.models.Asic;
import com.watts2crypto.watts2crypto_backend.models.RendimientoAlgoritmo;
import com.watts2crypto.watts2crypto_backend.service.AsicService;

@RestController
@RequestMapping("/api/asics")
public class AsicController {

	private final AsicService service;

	public AsicController(AsicService service) {
		this.service = service;
	}

	@GetMapping
	public List<Asic> getAllAsics() {
		return service.findAllAsics();
	}

	@GetMapping("/{nombre}")
	public Asic getAsicByName(@PathVariable String nombre) {
		return service.findAsicByName(nombre);
	}

	@GetMapping("/nombres")
	public List<String> getAllAsicNames() {
		return service.findAllAsicNames();
	}

	@GetMapping("/{nombre}/{algoritmo}")
	public RendimientoAlgoritmo getHashrateYConsumoPorNombreYAlgoritmo(@PathVariable String nombre,
																		@PathVariable String algoritmo) {
		return service.findHashrateYConsumoPorNombreYAlgoritmo(nombre, algoritmo);
	}
}
