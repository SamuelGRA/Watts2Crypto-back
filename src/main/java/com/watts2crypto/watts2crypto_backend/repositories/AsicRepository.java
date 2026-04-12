package com.watts2crypto.watts2crypto_backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.watts2crypto.watts2crypto_backend.models.Asic;
import com.watts2crypto.watts2crypto_backend.models.RendimientoAlgoritmo;

public interface AsicRepository extends BaseRepository<Asic> {

    @Query("SELECT a FROM Asic a WHERE LOWER(a.nombre) = LOWER(:name)")
    Optional<Asic> findByNameIgnoreCase(@Param("name") String name);

    @Query("SELECT a.nombre FROM Asic a")
    Optional<List<String>> findAllNames();

    @Query("""
           SELECT value(raEntry)
           FROM Asic a
           JOIN a.algoritmos raEntry
           WHERE LOWER(a.nombre) = LOWER(:name)
             AND LOWER(key(raEntry)) = LOWER(:algorithm)
           """)
    Optional<RendimientoAlgoritmo> findHashrateAndPowerByAsicAndAlgorithm(@Param("name") String name, @Param("algorithm") String algorithm);
}
