package com.watts2crypto.watts2crypto_backend.service;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.watts2crypto.watts2crypto_backend.models.Electricidad;
import com.watts2crypto.watts2crypto_backend.repositories.ElectricidadRepository;

import jakarta.annotation.PostConstruct;

@Service
public class ElectricidadService {

	private static final ZoneId ZONA_HORARIA = ZoneId.of("Europe/Madrid");

	private final ElectricidadRepository repository;
	private final RestTemplate restTemplate;

	private static final String energyChartsBaseUrl = "https://api.energy-charts.info/price";
	private static final List<String> zonasSoportadas = List.of(
			"AT", "BE", "BG", "CH", "CZ", "DE-LU", "DE-AT-LU",
			"DK1", "DK2", "EE", "ES", "FI", "FR", "GR", "HR", "HU",
			"IT-Calabria", "IT-Centre-North", "IT-Centre-South", "IT-North",
			"IT-SACOAC", "IT-SACODC", "IT-Sardinia", "IT-Sicily", "IT-South",
			"LT", "LV", "ME", "NL", "NO1", "NO2", "NO2NSL", "NO3", "NO4", "NO5",
			"PL", "PT", "RO", "RS", "SE1", "SE2", "SE3", "SE4", "SI", "SK");

	private static final List<String> zonasEnBD = List.of("AT", "BE", "BG", "CH", "CZ",
			"DK1", "EE", "ES", "FI", "FR", "GR", "HR", "HU", "IT-Centre-South",
			"LT", "LV", "ME", "NL", "NO1", "PL", "PT", "RO", "RS", "SE1", "SI", "SK");

	public ElectricidadService(ElectricidadRepository repository, RestTemplate restTemplate) {
		this.repository = repository;
		this.restTemplate = restTemplate;
	}

	@PostConstruct //Cambiar por shceduled
	public void initElectricidad() {
		if (repository.count() > 0) {
			return;
		}

		List<Electricidad> precios = cargarElectricidadDeEnergyCharts();
		repository.deleteAll();
		repository.saveAll(precios);
	}

	private List<Electricidad> cargarElectricidadDeEnergyCharts() {
		try {
			List<Electricidad> res = new ArrayList<>();

			for (String zona : zonasEnBD) {
				try {
					String body = llamarApiEnergyCharts(zona);
					res.addAll(parsearElectricidadDeEnergyCharts(body, zona));
				} catch (Exception e) {
					if (e.getMessage().toLowerCase().contains("goaway")) {
						System.err.println("Error transitorio cargando zona " + zona + ": " + e.getMessage());
						continue;
					}
					throw e;
				}
			}

			return res;
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(
					HttpStatus.INTERNAL_SERVER_ERROR,
					"Error cargando datos de electricidad de Energy-Charts: " + e.getMessage());
		}
	}

	private String llamarApiEnergyCharts(String zona) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");
		headers.set("User-Agent", "Mozilla/5.0");

		LocalDate hoy = LocalDate.now(ZONA_HORARIA);
		String start = String.valueOf(hoy.minusYears(1).atStartOfDay(ZONA_HORARIA).toEpochSecond());
		String end = String.valueOf(hoy.atStartOfDay(ZONA_HORARIA).toEpochSecond());

		HttpEntity<Void> entity = new HttpEntity<>(headers);
		String url = energyChartsBaseUrl
				+ "?bzn=" + zona
				+ "&start=" + start
				+ "&end=" + end;

		int maxIntentos = 3;
		for (int intento = 1; intento <= maxIntentos; intento++) {
			try {
				ResponseEntity<String> response = restTemplate.exchange(
						url,
						HttpMethod.GET,
						entity,
						String.class);
				return response.getBody();
			} catch (Exception e) {
				boolean transitorio = e.getMessage().toLowerCase().contains("goaway");
				if (transitorio && intento < maxIntentos) {
					continue;
				}
				throw e;
			}
		}

		throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
				"No se pudo completar la llamada a Energy-Charts para la zona " + zona);
	}

	public List<Electricidad> findElectricidadDirectaPorZona(String zona) {
		try {
			String zonaNormalizada = normalizarZonaSoportada(zona);
			String body = llamarApiEnergyCharts(zonaNormalizada);
			if (body == null || body.isBlank()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND,
						"No se encontraron datos para la zona indicada");
			}
			return parsearElectricidadDeEnergyCharts(body, zonaNormalizada);
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	private String normalizarZonaSoportada(String zona) {
		if (zona == null || zona.isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Zona no soportada por la API");
		}

		String limpia = zona.trim();
		for (String zonaSoportada : zonasSoportadas) {
			if (zonaSoportada.equalsIgnoreCase(limpia)) {
				return zonaSoportada;
			}
		}

		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Zona no soportada por la API");
	}

	private List<Electricidad> parsearElectricidadDeEnergyCharts(String body, String zona) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(body);

			List<Long> timestamps = extraerListaTimestamps(root);
			List<Double> precios = extraerListaPrecios(root);

			int limite = Math.min(timestamps.size(), precios.size());
			Map<LocalDate, List<Double>> preciosPorDia = new HashMap<>();

			for (int i = 0; i < limite; i++) {
				Long rawTimestamp = timestamps.get(i);
				Double precio = precios.get(i);
				if (rawTimestamp == null || precio == null || precio < 0) {
					continue;
				}

				LocalDate dia = convertirTimestamp(rawTimestamp).atZone(ZONA_HORARIA).toLocalDate();
				preciosPorDia.computeIfAbsent(dia, k -> new ArrayList<>()).add(precio);
			}

			return preciosPorDia.entrySet().stream()
					.map(entry -> {
						LocalDate dia = entry.getKey();
						double media = entry.getValue().stream()
								.mapToDouble(Double::doubleValue)
								.average()
								.orElse(0.0);
						double mediaRedondeada = Math.round(media * 100.0) / 100.0;
						if (mediaRedondeada <= 0.0) {
							return null;
						}

						Electricidad electricidad = new Electricidad();
						electricidad.setZona(zona);
						electricidad.setFecha(dia.atStartOfDay());
						electricidad.setPrecioMwh(mediaRedondeada);
						return electricidad;
					})
					.filter(electricidad -> electricidad != null)
					.sorted(Comparator.comparing(Electricidad::getFecha))
					.toList();
		} catch (IOException e) {
			throw new IllegalStateException("Error parseando JSON de Energy-Charts", e);
		}
	}

	private List<Long> extraerListaTimestamps(JsonNode root) {
		List<Long> timestamps = new ArrayList<>();

		if (!root.isObject()) {
			return timestamps;
		}

		for (JsonNode value : root) {
			if (value.isArray() && arrayPareceDeTimestamps(value)) {
				for (JsonNode item : value) {
					timestamps.add(item.asLong());
				}
				break;
			}
		}

		return timestamps;
	}

	private List<Double> extraerListaPrecios(JsonNode root) {
		List<Double> precios = new ArrayList<>();

		if (!root.isObject()) {
			return precios;
		}

		for (JsonNode value : root) {
			if (value.isArray() && !arrayPareceDeTimestamps(value)) {
				for (JsonNode item : value) {
					precios.add(item.asDouble());
				}
				break;
			}
		}

		return precios;
	}

	private boolean arrayPareceDeTimestamps(JsonNode arrayNode) {
		if (!arrayNode.isArray() || arrayNode.isEmpty()) {
			return false;
		}

		JsonNode primerElemento = arrayNode.get(0);
		return primerElemento.isNumber() && primerElemento.asLong() > 1_000_000_000L;
	}

	private Instant convertirTimestamp(long rawTimestamp) {
		if (rawTimestamp > 10_000_000_000L) {
			return Instant.ofEpochMilli(rawTimestamp);
		}
		return Instant.ofEpochSecond(rawTimestamp);
	}

	public List<Electricidad> findAll() {
		try {
			List<Electricidad> res = repository.findAll();
			if(res.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron datos de electricidad");
			}
			return res;
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	public List<Electricidad> findByDateRangeAndZone(String zona, LocalDateTime start, LocalDateTime end) {
		try {
			List<Electricidad> res = repository.findByDateRange(zona, start, end);
			if (res.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND,
						"No se encontraron resultados para esos parámetros");
			}
			return res;
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	public Double findPriceByExactDateAndZone(String zone, LocalDateTime date) {
		try {
			Optional<Double> res = repository.findPriceBydateAndZone(zone, date);
			if(!res.isPresent()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Precio no encontrado para la zona y fecha indicadas");
			}
			return res.get();
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	public Electricidad findLatestPriceInZone(String zone) {
		try {
			Optional<Electricidad> res = repository.findFirstByZonaOrderByFechaDesc(zone);
			if(!res.isPresent()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No hy datos de precios para la zona indicada");
			}
			return res.get();
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	public List<String> findAllZones() {
		try {
			List<String> res = repository.findAllZoneNames();
			if (res.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron los nombres de las zonas");
			}
			return res;
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	public List<String> findZonesForDirectSearch() {
		try {
			List<String> res = new ArrayList<>(zonasSoportadas);
			List<String> zonasPersistidas = findAllZones();
			res.removeAll(zonasPersistidas);
			return res;
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

}
