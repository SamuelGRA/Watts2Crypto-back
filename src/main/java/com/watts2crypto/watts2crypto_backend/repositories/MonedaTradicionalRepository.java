package com.watts2crypto.watts2crypto_backend.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.watts2crypto.watts2crypto_backend.models.MonedaTradicional;

public interface MonedaTradicionalRepository extends BaseRepository<MonedaTradicional> {

    @Query("SELECT mt FROM MonedaTradicional mt WHERE mt.monedaBase = :symbol")
    Optional<MonedaTradicional> findBaseBySymbol(@Param("symbol") String symbol);

    @Query("SELECT mt FROM MonedaTradicional mt WHERE mt.fecha BETWEEN :startdate AND :endDate")
    Optional<List<MonedaTradicional>> findAllBetweenDates(@Param("startDate") LocalDateTime startDate, 
    @Param("endDate") LocalDateTime endDate);
}
