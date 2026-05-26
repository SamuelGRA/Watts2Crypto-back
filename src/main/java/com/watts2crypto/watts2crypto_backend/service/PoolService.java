package com.watts2crypto.watts2crypto_backend.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.watts2crypto.watts2crypto_backend.models.Pool;
import com.watts2crypto.watts2crypto_backend.models.PoolMonedaComision;
import com.watts2crypto.watts2crypto_backend.repositories.PoolMonedaComisionRepository;
import com.watts2crypto.watts2crypto_backend.repositories.PoolRepository;

@Service
public class PoolService {

    private final PoolRepository repository;
    private final PoolMonedaComisionRepository poolMonedaComisionRepository;

    public PoolService(PoolRepository repository, PoolMonedaComisionRepository poolMonedaComisionRepository) {
        this.repository = repository;
        this.poolMonedaComisionRepository = poolMonedaComisionRepository;
    }

    public void refreshPools() {
        try {
            poolMonedaComisionRepository.deleteAll();
            scrapearPoolsHashrateNo();
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private void scrapearPoolsHashrateNo() throws IOException {
        List<Pool> pools = repository.findAll();

        for (Pool pool : pools) {
            if (pool.getHashrateSlug() == null || pool.getHashrateSlug().isEmpty()) {
                System.out.println(pool.getNombre() + " no tiene hashrate_slug, saltando...");
                continue;
            }

            try {
                scrapearPoolIndividual(pool);
            } catch (IOException e) {
                System.err.println("Error procesando " + pool.getNombre() + ": " + e.getMessage());
            }
        }
    }

    private void scrapearPoolIndividual(Pool pool) throws IOException {
        String url = "https://www.hashrate.no/pools/" + pool.getHashrateSlug();
        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0")
                .timeout(15000)
                .get();

        Elements contenedores = doc.select("div.w3-row.estimate");

        for (Element contenedor : contenedores) {
            String moneda = contenedor.selectFirst("span.brand").text().trim();
            if (moneda.isEmpty()) {
                continue;
            }

            Element columnaComision = contenedor.selectFirst("div.w3-col.l2.m4.s4");
            if (columnaComision == null) {
                continue;
            }
            String comisionStr = columnaComision.selectFirst("div.estimates").text()
                    .replaceAll("[^0-9.]", "").trim();
            if (comisionStr.isEmpty()) {
                continue;
            }

            try {
                Double comision = Double.parseDouble(comisionStr);

                Optional<PoolMonedaComision> existente = poolMonedaComisionRepository
                        .findByPoolIdAndMoneda(pool.getId(), moneda);

                if (existente.isEmpty()) {
                    PoolMonedaComision registro = new PoolMonedaComision(pool, moneda, comision);
                    poolMonedaComisionRepository.save(registro);
                }
            } catch (NumberFormatException e) {
                System.err.println("Error parseando comisión para " + moneda + " en " + pool.getNombre());
            }
        }
    }

    public List<Pool> findAll() {
        try {
            List<Pool> res = repository.findAll();
            if (res.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron pools de minería");
            }
            return res;
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    public Pool findByName(String nombre) {
        try {
            Optional<Pool> res = repository.findByName(nombre);
            if (!res.isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pool " + nombre + " no encontrado");
            }
            return res.get();
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    public List<String> findAllNames() {
        try {
            List<String> res = repository.findAllNames();
            if (res.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron pools de minería");
            }
            return res;
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    public List<String> findAvailablePoolsByAlgorithm(String algoritmo) {
        try {
            List<String> res = repository.findNamesByAlgorithm(algoritmo);
            if (res.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No hay ningún pool que soporte el algoritmo " + algoritmo);
            }
            return res;
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
