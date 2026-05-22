package com.watts2crypto.watts2crypto_backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.watts2crypto.watts2crypto_backend.models.Cpu;
import com.watts2crypto.watts2crypto_backend.service.CpuService;

@RestController
@RequestMapping("/api/cpus")
public class CpuController {

	private final CpuService service;

	public CpuController(CpuService service) {
		this.service = service;
	}

	@GetMapping
	public List<Cpu> getAllCpus() {
		return service.findAllCpus();
	}

	@GetMapping("/{nombre}")
	public Cpu getCpuByName(@PathVariable String nombre) {
		return service.findCpuByName(nombre);
	}

    @GetMapping("/nombres")
    public List<String> getAllCpuNames() {
        return service.findAllCpuNames();
    }

    @GetMapping("/byAlgoritmo/{algoritmo}")
    public List<String> getCpusByAlgorithm(@PathVariable String algoritmo) {
        return service.findNamesByAlgorithm(algoritmo);
    }
}