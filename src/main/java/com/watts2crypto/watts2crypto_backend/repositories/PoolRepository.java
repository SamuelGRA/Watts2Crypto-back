package com.watts2crypto.watts2crypto_backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.watts2crypto.watts2crypto_backend.models.Pool;

public interface PoolRepository extends BaseRepository<Pool> {

    @Query("SELECT p FROM Pool p WHERE LOWER(p.nombre) = LOWER(:name)")
    Optional<Pool> findByName(@Param("name") String name);

    @Query("SELECT p.nombre FROM Pool p")
    List<String> findAllNames();

    @Query("""
            SELECT DISTINCT p.nombre
            FROM Pool p
            JOIN p.detallesMonedaComision pmc
            JOIN MetricasMinado mm ON LOWER(mm.nombreMoneda) = LOWER(pmc.moneda)
            WHERE LOWER(mm.algoritmo) = LOWER(:algoritmo)
            """)
    List<String> findNamesByAlgorithm(@Param("algoritmo") String algoritmo);
}
