package com.watts2crypto.watts2crypto_backend.service;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.watts2crypto.watts2crypto_backend.models.Cpu;
import com.watts2crypto.watts2crypto_backend.repositories.CpuRepository;

@Service
public class CpuService {

    private final CpuRepository repository;

    public CpuService(CpuRepository repository) {
        this.repository = repository;
    }

    private static final String HASHRATE_NO_URL = "https://www.hashrate.no/coins/XMR/benchmarks";

    public void refreshCpus() {
        repository.deleteAll();
        try {
            scrapearHashrateNo();
        } catch (ResponseStatusException e) {
            throw e;
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private void scrapearHashrateNo() throws IOException {
        Document doc = Jsoup.connect(HASHRATE_NO_URL)
                .userAgent("Mozilla/5.0")
                .timeout(15000)
                .get();

        // Tabla hashrate: primera tabla
        // Tabla eficiencia: segunda tabla
        Elements tablas = doc.select("table");

        Map<String, Double> hashrates = parsearTabla(tablas.get(0)); // Kh/s
        Map<String, Double> eficiencias = parsearTabla(tablas.get(1)); // Kh/W

        for (String nombre : hashrates.keySet()) {
            Double hashrate = hashrates.get(nombre) * 1000; // convertir Kh/s a H/s
            Double eficiencia = eficiencias.get(nombre);
            Integer consumo = null;
            if (eficiencia != null && eficiencia > 0) {
                consumo = (int) Math.round(hashrate / (eficiencia * 1000)); // W
            }
            Cpu cpu = new Cpu(nombre, consumo, hashrate.intValue());
            repository.save(cpu);
        }
    }

    private Map<String, Double> parsearTabla(Element tabla) {
        Map<String, Double> resultado = new LinkedHashMap<>();
        for (Element fila : tabla.select("tr")) {
            Elements celdas = fila.select("td");
            String nombre = celdas.get(0).text().trim();
            if (nombre.toLowerCase().contains("antminer"))
                continue; // Elimina los ASICs que pueda haber
            String valorStr = celdas.get(1).text()
                    .replaceAll("[^0-9.]", "").trim();
            if (nombre.isEmpty() || valorStr.isEmpty())
                continue;
            try {
                resultado.put(nombre, Double.parseDouble(valorStr));
            } catch (NumberFormatException e) {
                System.err.println("Error parseando valor para: " + nombre);
            }
        }
        return resultado;
    }

    public List<Cpu> findAllCpus() {
        try {
            return repository.findAll();
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    public List<String> findAllCpuNames() {
        try {
            Optional<List<String>> res = repository.findAllNames();
            if (!res.isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No se pudieron obtener los nombres de los procesadores.");
            }
            return res.get();
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public Cpu findCpuByName(String name) {
        try {
            Optional<Cpu> res = repository.findByNameIgnoreCase(name);
            if (!res.isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Procesador " + name + " no encontrado.");
            }
            return res.get();
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());

        }
    }

    public List<String> findNamesByAlgorithm(String algoritmo) {
        try {
            String algoNormalizado = algoritmo.trim().toLowerCase();
            // Las CPU solo son compatibles con RandomX
            if (!algoNormalizado.contains("randomx")) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Las CPUs solo son compatibles con el algoritmo RandomX.");
            }
            return findAllCpuNames();
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
