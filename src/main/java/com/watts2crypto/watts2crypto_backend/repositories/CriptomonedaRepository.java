package com.watts2crypto.watts2crypto_backend.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.watts2crypto.watts2crypto_backend.models.Criptomoneda;

public interface CriptomonedaRepository extends BaseRepository<Criptomoneda> {

    @Query("SELECT c FROM Criptomoneda c WHERE LOWER(c.nombre) = LOWER(:name)")
    Optional<Criptomoneda> findByNameIgnoreCase(@Param("name") String name);

    @Query("SELECT c.nombre FROM Criptomoneda c")
    Optional<List<String>> findAllNames();

    @Query("SELECT c FROM Criptomoneda c WHERE c.timestamp BETWEEN :startdate AND :endDate")
    Optional<List<Criptomoneda>> findAllBetweenTimestamps(@Param("startDate") LocalDateTime startDate, 
    @Param("endDate") LocalDateTime endDate);

}
