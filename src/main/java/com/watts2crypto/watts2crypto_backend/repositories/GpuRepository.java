package com.watts2crypto.watts2crypto_backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.watts2crypto.watts2crypto_backend.models.Gpu;

public interface GpuRepository extends BaseRepository<Gpu> {

    @Query("SELECT g FROM Gpu g WHERE LOWER(g.nombre) = LOWER(:name)")
    Optional<Gpu> findByNameIgnoreCase(@Param("name") String name);

    @Query("SELECT g.nombre FROM Gpu g")
    Optional<List<String>> findAllNames();
    
}
