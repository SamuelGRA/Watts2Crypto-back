package com.watts2crypto.watts2crypto_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.watts2crypto.watts2crypto_backend.service.MaintenanceStateService;

@RestController
@RequestMapping("/api/maintenance")
public class MaintenanceController {

	private final MaintenanceStateService maintenanceStateService;

	public MaintenanceController(MaintenanceStateService maintenanceStateService) {
		this.maintenanceStateService = maintenanceStateService;
	}

	@GetMapping
	public ResponseEntity<MaintenanceStatusResponse> getMaintenanceStatus() {
		if (maintenanceStateService.isRefreshing()) {
			return ResponseEntity.ok(new MaintenanceStatusResponse(
				true,
				"Estamos actualizando nuestros datos, intente acceder más tarde"
				,
				maintenanceStateService.getActiveRefreshes()
			));
		}

		return ResponseEntity.ok(new MaintenanceStatusResponse(false, null, maintenanceStateService.getActiveRefreshes()));
	}
}