package com.watts2crypto.watts2crypto_backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.watts2crypto.watts2crypto_backend.models.ElectricidadPorPais;

public interface ElectricidadPorPaisRepository extends BaseRepository<ElectricidadPorPais>{

    @Query("SELECT epp.precioKwh FROM ElectricidadPorPais epp WHERE LOWER(epp.pais) = LOWER(:pais)")
    Optional<Double> findPrecioByPais(@Param("pais") String pais);
    
}
