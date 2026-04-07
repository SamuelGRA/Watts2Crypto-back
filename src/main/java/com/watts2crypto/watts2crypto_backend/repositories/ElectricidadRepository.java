package com.watts2crypto.watts2crypto_backend.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.watts2crypto.watts2crypto_backend.models.Electricidad;

public interface ElectricidadRepository extends BaseRepository<Electricidad> {

     @Query("""
           SELECT e
           FROM Electricidad e
           WHERE e.zona = :zona
             AND e.fecha BETWEEN :startDate AND :endDate
           ORDER BY e.fecha ASC
           """)
    List<Electricidad> findByDateRange(
            @Param("zona") String zona,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    Optional<Electricidad> findFirstByZonaOrderByFechaDesc(String zona); //No especifico query porque en esta puede dar problemas

    @Query("""
           SELECT e.precioMwh
           FROM Electricidad e
           WHERE e.zona = :zona
             AND e.fecha = :fecha
           """)
    Optional<Double> findPriceBydateAndZone(
            @Param("zona") String zona,
            @Param("fecha") LocalDateTime fecha
    );

    @Query("SELECT DISTINCT e.zona FROM Electricidad e ORDER BY e.zona")
    List<String> findAllZoneNames();
}
