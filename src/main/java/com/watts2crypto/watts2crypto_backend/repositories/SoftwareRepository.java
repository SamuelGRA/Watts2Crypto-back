package com.watts2crypto.watts2crypto_backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.watts2crypto.watts2crypto_backend.models.Software;

public interface SoftwareRepository extends BaseRepository<Software> {

    @Query("SELECT s FROM Software s WHERE LOWER(s.nombre) = lower(:name)")
    Optional<Software> findByName(@Param("name") String name);

    @Query("SELECT s.nombre FROM Software s")
    List<String> findAllNames();

    @Query("""
            SELECT DISTINCT s.nombre
            FROM Software s
            JOIN s.detallesAlgoritmoMoneda sam
            WHERE LOWER(sam.algoritmo) = LOWER(:algoritmo)
            """)
    List<String> findNamesByAlgorithm(@Param("algoritmo") String algoritmo);
}
