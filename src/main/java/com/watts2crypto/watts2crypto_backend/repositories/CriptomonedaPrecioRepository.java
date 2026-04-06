package com.watts2crypto.watts2crypto_backend.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.watts2crypto.watts2crypto_backend.models.CriptomonedaPrecio;

public interface CriptomonedaPrecioRepository extends BaseRepository<CriptomonedaPrecio> {

    @Query("""
            SELECT p FROM CriptomonedaPrecio p
            WHERE LOWER(p.criptomoneda.nombre) = LOWER(:nombre)
              AND p.fecha BETWEEN :startDate AND :endDate
            ORDER BY p.fecha ASC
            """)
    List<CriptomonedaPrecio> findByNombreAndDateRange(
            @Param("nombre") String nombre,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("""
            SELECT p FROM CriptomonedaPrecio p
            WHERE LOWER(p.criptomoneda.assetId) = LOWER(:assetId)
            ORDER BY p.fecha DESC
            """)
    List<CriptomonedaPrecio> findByAssetIdOrderByFechaDesc(@Param("assetId") String assetId);
}
