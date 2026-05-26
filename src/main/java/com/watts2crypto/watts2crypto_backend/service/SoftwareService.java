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

import com.watts2crypto.watts2crypto_backend.models.Software;
import com.watts2crypto.watts2crypto_backend.models.SoftwareAlgoritmoMoneda;
import com.watts2crypto.watts2crypto_backend.repositories.SoftwareAlgoritmoMonedaRepository;
import com.watts2crypto.watts2crypto_backend.repositories.SoftwareRepository;

@Service
public class SoftwareService {

    private final SoftwareRepository repository;
    private final SoftwareAlgoritmoMonedaRepository softwareAlgoritmoMonedaRepository;

    public SoftwareService(SoftwareRepository repository,
            SoftwareAlgoritmoMonedaRepository softwareAlgoritmoMonedaRepository) {
        this.repository = repository;
        this.softwareAlgoritmoMonedaRepository = softwareAlgoritmoMonedaRepository;
    }

    public void refreshSoftware() {
        try {
            softwareAlgoritmoMonedaRepository.deleteAll();
            scrapearSoftwaresHashrateNo();
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private void scrapearSoftwaresHashrateNo() throws IOException {
        List<Software> softwares = repository.findAll();

        for (Software software : softwares) {
            if (software.getHashrateSlug() == null || software.getHashrateSlug().isEmpty()) {
                System.out.println(software.getNombre() + " no tiene hashrate_slug, saltando...");
                continue;
            }

            try {
                scrapearSoftwareIndividual(software);
            } catch (IOException e) {
                System.err.println("Error procesando " + software.getNombre() + ": " + e.getMessage());
            }
        }
    }

    private void scrapearSoftwareIndividual(Software software) throws IOException {
        String url = "https://www.hashrate.no/miners/" + software.getHashrateSlug();
        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0")
                .timeout(15000)
                .get();

        Elements contenedores = doc.select("div.w3-row.estimate");

        for (Element contenedor : contenedores) {
            Element monedaEl = contenedor.selectFirst("span.brand");
            if (monedaEl == null) {
                continue;
            }
            String moneda = monedaEl.text().trim();
            if (moneda.isEmpty())
                continue;

            Element columnaAlgoritmo = contenedor.selectFirst("div.w3-col.l7.m7.s7");
            if (columnaAlgoritmo == null) {
                continue;
            }
            Element algoritmoEl = columnaAlgoritmo.selectFirst("div.estimates");
            if (algoritmoEl == null) {
                continue;
            }
            String algoritmo = algoritmoEl.text().trim();
            if (algoritmo.isEmpty())
                continue;

            Element columnaComision = contenedor.selectFirst("div.w3-col.l5.m5.s5");
            if (columnaComision == null) {
                continue;
            }
            Element comisionEl = columnaComision.selectFirst("div.estimates");
            if (comisionEl == null) {
                continue;
            }
            String comisionStr = comisionEl.text()
                    .replaceAll("[^0-9.]", "").trim();
            if (comisionStr.isEmpty())
                continue;

            try {
                Double comision = Double.parseDouble(comisionStr);

                // Verificar que no exista ya ese registro
                Optional<SoftwareAlgoritmoMoneda> existente = softwareAlgoritmoMonedaRepository
                        .findBySoftwareIdAndMoneda(software.getId(), moneda);

                if (existente.isEmpty()) {
                    SoftwareAlgoritmoMoneda registro = new SoftwareAlgoritmoMoneda(
                            software, moneda, algoritmo, comision);
                    softwareAlgoritmoMonedaRepository.save(registro);
                }
            } catch (NumberFormatException e) {
                System.err.println("Error parseando comisión para " + moneda + " en " + software.getNombre());
            }
        }
    }

    public List<Software> findAll() {
        try {
            List<Software> res = repository.findAll();
            if (res.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron sofwtares de minería");
            }
            return res;
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    public Software findByName(String nombre) {
        try {
            Optional<Software> res = repository.findByName(nombre);
            if (!res.isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Software " + nombre + " no encontrado");
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
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron sofwtares de minería");
            }
            return res;
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    public List<String> findAvailableSoftwaresByAlgorithm(String algoritmo) {
        try {
            List<String> res = repository.findNamesByAlgorithm(algoritmo);
            if (res.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No hay ningún software que soporte el algoritmo " + algoritmo);
            }
            return res;
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }
}
