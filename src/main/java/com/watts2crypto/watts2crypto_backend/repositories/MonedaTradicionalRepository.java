package com.watts2crypto.watts2crypto_backend.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.watts2crypto.watts2crypto_backend.models.MonedaTradicional;

public interface MonedaTradicionalRepository extends BaseRepository<MonedaTradicional> {

        @Query("""
                        SELECT COUNT(m) > 0 FROM MonedaTradicional m WHERE m.monedaBase = :monedaBase
                            AND m.monedaObjetivo = :monedaObjetivo
                            AND m.fecha = :fecha
                        """)
        boolean existsEntry(@Param("monedaBase") String monedaBase, @Param("monedaObjetivo") String monedaObjetivo,
                        @Param("fecha") LocalDate fecha);

        @Query("""
                        SELECT m FROM MonedaTradicional m
                        WHERE m.monedaBase = :monedaBase
                          AND m.monedaObjetivo = :monedaObjetivo
                          AND m.fecha = :fecha
                        """)
        Optional<MonedaTradicional> findByExactDate(@Param("monedaBase") String monedaBase,
                        @Param("monedaObjetivo") String monedaObjetivo,
                        @Param("fecha") LocalDate fecha);

        @Query("""
                        SELECT m FROM MonedaTradicional m
                        WHERE m.monedaBase = :monedaBase
                          AND m.monedaObjetivo = :monedaObjetivo
                          AND m.fecha BETWEEN :fechaInicio AND :fechaFin
                        ORDER BY m.fecha
                        """)
        List<MonedaTradicional> findByDateRange(@Param("monedaBase") String monedaBase,
                        @Param("monedaObjetivo") String monedaObjetivo,
                        @Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);
        
}
