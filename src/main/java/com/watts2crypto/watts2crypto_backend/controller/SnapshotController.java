package com.watts2crypto.watts2crypto_backend.controller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.util.StringUtils;

import com.watts2crypto.watts2crypto_backend.service.SnapshotService;

@RestController
@RequestMapping("/api/snapshot")
public class SnapshotController {
	@Value("${refresh.token:}")
	private String refreshDataToken;

	private final Environment environment;
	private final SnapshotService snapshotService;

	public SnapshotController(SnapshotService snapshotService, Environment environment) {
		this.snapshotService = snapshotService;
		this.environment = environment;
	}

	@GetMapping(value = "/export", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<StreamingResponseBody> exportSnapshot(
			@RequestHeader(value = "X-Refresh-Token", required = false) String refreshToken,
			@RequestHeader(value = "Authorization", required = false) String authorization) {
		validarAccesoAlExport(refreshToken, authorization);

		StreamingResponseBody body = outputStream -> {
			try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8))) {
				snapshotService.writeSnapshot(writer);
			} catch (java.sql.SQLException ex) {
				throw new IOException("No se pudo exportar la snapshot.", ex);
			}
		};

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION,
						ContentDisposition.attachment().filename("watts2crypto-snapshot.sql").build().toString())
				.contentType(MediaType.TEXT_PLAIN)
				.body(body);
	}

	@PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Map<String, String>> importSnapshot(@RequestParam("file") MultipartFile file) throws IOException {
		if (isProd()) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "La importación de snapshots solo está disponible en despliegues locales.");
		}

		try {
			snapshotService.importSnapshot(file);
			return ResponseEntity.status(HttpStatus.OK).body(Map.of(
					"status", "ok",
					"message", "Snapshot importada correctamente. Ejecuta 'docker compose restart' para reiniciar la app."));
		} catch (java.sql.SQLException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo importar la snapshot.", ex);
		}
	}

	private void validarAccesoAlExport(String refreshToken, String authorization) {
		if (!StringUtils.hasText(refreshDataToken)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN,
					"No se permite exportar la snapshot desde entornos locales.");
		}

		String tokenProporcionado = null;
		if (StringUtils.hasText(refreshToken)) {
			tokenProporcionado = refreshToken.trim();
		} else if (StringUtils.hasText(authorization)) {
			String valor = authorization.trim();
			if (valor.regionMatches(true, 0, "Bearer ", 0, 7)) {
				tokenProporcionado = valor.substring(7).trim();
			} else {
				tokenProporcionado = valor;
			}
		}

		if (!refreshDataToken.equals(tokenProporcionado)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"No autorizado para exportar snapshots.");
		}
	}

	private boolean isProd() {
		return java.util.Arrays.stream(environment.getActiveProfiles())
				.anyMatch(profile -> "prod".equalsIgnoreCase(profile));
	}
}