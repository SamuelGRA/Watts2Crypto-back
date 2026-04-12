package com.watts2crypto.watts2crypto_backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.watts2crypto.watts2crypto_backend.models.Pool;
import com.watts2crypto.watts2crypto_backend.service.PoolService;

@RestController
@RequestMapping("/api/pools")
public class PoolController {

	private final PoolService service;

	public PoolController(PoolService service) {
		this.service = service;
	}

	@GetMapping
	public List<Pool> getAllPools() {
		return service.findAll();
	}

	@GetMapping("/{nombre}")
	public Pool getPoolByName(@PathVariable String nombre) {
		return service.findByName(nombre);
	}

	@GetMapping("/nombres")
	public List<String> getAllPoolNames() {
		return service.findAllNames();
	}

	@GetMapping("/byAlgoritmo/{algoritmo}")
	public List<String> getPoolsByAlgorithm(@PathVariable String algoritmo) {
		return service.findAvailablePoolsByAlgorithm(algoritmo);
	}
}
