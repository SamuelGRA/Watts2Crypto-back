package com.watts2crypto.watts2crypto_backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.watts2crypto.watts2crypto_backend.models.SoftwareAlgoritmoMoneda;

public interface SoftwareAlgoritmoMonedaRepository extends JpaRepository<SoftwareAlgoritmoMoneda, Long> {

    @Query("""
            SELECT sam FROM SoftwareAlgoritmoMoneda sam
            WHERE sam.software.id = :softwareId
            AND LOWER(sam.moneda) = LOWER(:moneda)
            """)
    Optional<SoftwareAlgoritmoMoneda> findBySoftwareIdAndMoneda(
            @Param("softwareId") Long softwareId,
            @Param("moneda") String moneda);

    @Query("""
            SELECT DISTINCT sam.algoritmo FROM SoftwareAlgoritmoMoneda sam
            WHERE sam.software.id = :softwareId
            """)
    List<String> findAlgoritmosBySoftwareId(@Param("softwareId") Long softwareId);

    @Query("""
            SELECT DISTINCT sam.moneda FROM SoftwareAlgoritmoMoneda sam
            WHERE sam.software.id = :softwareId
            """)
    List<String> findMonedasBySoftwareId(@Param("softwareId") Long softwareId);

    @Query("""
            SELECT sam.comision FROM SoftwareAlgoritmoMoneda sam
            WHERE sam.software.id = :softwareId
            AND LOWER(sam.moneda) = LOWER(:moneda)
            """)
    Optional<Double> findComisionBySoftwareIdAndMoneda(
            @Param("softwareId") Long softwareId,
            @Param("moneda") String moneda);
}
