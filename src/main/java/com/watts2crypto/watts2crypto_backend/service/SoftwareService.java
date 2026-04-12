package com.watts2crypto.watts2crypto_backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.watts2crypto.watts2crypto_backend.models.Software;
import com.watts2crypto.watts2crypto_backend.repositories.SoftwareRepository;

@Service
public class SoftwareService {
    
    private final SoftwareRepository repository;

    public SoftwareService(SoftwareRepository repository) {
        this.repository = repository;
    }

    public List<Software> findAll() {
        try {
            List<Software> res = repository.findAll();
            if(res.isEmpty()) {
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
            if(!res.isPresent()) {
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
            if(res.isEmpty()) {
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
            if(res.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No hay ningún software que soporte el algoritmo " + algoritmo);
            }
            return res;
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }
}
