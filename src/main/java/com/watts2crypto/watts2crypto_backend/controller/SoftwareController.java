package com.watts2crypto.watts2crypto_backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.watts2crypto.watts2crypto_backend.models.Software;
import com.watts2crypto.watts2crypto_backend.service.SoftwareService;

@RestController
@RequestMapping("/api/software")
public class SoftwareController {

	private final SoftwareService service;

	public SoftwareController(SoftwareService service) {
		this.service = service;
	}

	@GetMapping
	public List<Software> getAllSoftware() {
		return service.findAll();
	}

	@GetMapping("/{nombre}")
	public Software getSoftwareByName(@PathVariable String nombre) {
		return service.findByName(nombre);
	}

	@GetMapping("/nombres")
	public List<String> getAllSoftwareNames() {
		return service.findAllNames();
	}

	@GetMapping("/byAlgoritmo/{algoritmo}")
	public List<String> getSoftwareByAlgorithm(@PathVariable String algoritmo) {
		return service.findAvailableSoftwaresByAlgorithm(algoritmo);
	}
}
