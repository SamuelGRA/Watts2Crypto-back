package com.watts2crypto.watts2crypto_backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.watts2crypto.watts2crypto_backend.models.Cpu;

public interface CpuRepository extends BaseRepository<Cpu> {

    @Query("SELECT c FROM Cpu c WHERE LOWER(c.nombre) = LOWER(:name)")
    Optional<Cpu> findByNameIgnoreCase(@Param("name") String name);

    @Query("SELECT c.nombre FROM Cpu c")
    Optional<List<String>> findAllNames();
    
}
