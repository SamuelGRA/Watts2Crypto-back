package com.watts2crypto.watts2crypto_backend.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.watts2crypto.watts2crypto_backend.models.Electricidad;

public interface ElectricidadRepository extends BaseRepository<Electricidad> {

    @Query("SELECT e.precioMwh FROM Electricidad e WHERE e.zona = :zona")
    Double getPrecioPorZona(@Param("zona") String zona);

    @Query("SELECT e FROM Electricidad e WHERE e.fecha BETWEEN :startdate AND :endDate")
    Optional<List<Electricidad>> findAllBetweenDates(@Param("startDate") LocalDateTime startDate, 
    @Param("endDate") LocalDateTime endDate);
}
