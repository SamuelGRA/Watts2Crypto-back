package com.watts2crypto.watts2crypto_backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.watts2crypto.watts2crypto_backend.models.Pool;
import com.watts2crypto.watts2crypto_backend.repositories.PoolRepository;

@Service
public class PoolService {

    private final PoolRepository repository;

    public PoolService(PoolRepository repository) {
        this.repository = repository;
    }

    public List<Pool> findAll() {
        try {
            List<Pool> res = repository.findAll();
            if(res.isEmpty()) {
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
            if(!res.isPresent()) {
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
            if(res.isEmpty()) {
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
            if(res.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No hay ningún pool que soporte el algoritmo " + algoritmo);
            }
            return res;
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    
}
