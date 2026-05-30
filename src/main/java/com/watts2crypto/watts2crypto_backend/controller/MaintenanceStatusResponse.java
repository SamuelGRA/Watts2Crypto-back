package com.watts2crypto.watts2crypto_backend.controller;

import java.util.List;

public record MaintenanceStatusResponse(
		boolean maintenanceMode,
		String message,
		List<String> activeRefreshes,
		boolean directCryptoAvailable) {
}