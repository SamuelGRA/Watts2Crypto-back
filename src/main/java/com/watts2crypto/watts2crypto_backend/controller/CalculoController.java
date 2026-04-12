package com.watts2crypto.watts2crypto_backend.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.watts2crypto.watts2crypto_backend.models.DTOs.CalculoInputDto;
import com.watts2crypto.watts2crypto_backend.models.DTOs.CalculoOutputDto;
import com.watts2crypto.watts2crypto_backend.service.CalculoService;

@RestController
@RequestMapping("/api/calculo")
public class CalculoController {

	private final CalculoService service;

	public CalculoController(CalculoService service) {
		this.service = service;
	}

	@PostMapping("/rentabilidad")
	public CalculoOutputDto calcularRentabilidad(@RequestBody CalculoInputDto input) {
		return service.calcularRentabilidad(input);
	}
}
