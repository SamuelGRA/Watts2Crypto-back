package com.watts2crypto.watts2crypto_backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

@Service
public class DirectRequestAvailabilityService {

	private final String coincapApiKey;

	public DirectRequestAvailabilityService(@Value("${coincap.api.key:}") String coincapApiKey) {
		this.coincapApiKey = coincapApiKey;
	}

	public boolean isDirectCryptoAvailable() {
		return StringUtils.hasText(coincapApiKey);
	}

	public void assertDirectCryptoAvailable() {
		if (!isDirectCryptoAvailable()) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN,
					"La búsqueda a demanda de criptomonedas está desactivada para despliegues locales.");
		}
	}
}