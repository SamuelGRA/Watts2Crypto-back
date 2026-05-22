package com.watts2crypto.watts2crypto_backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.watts2crypto.watts2crypto_backend.models.PoolMonedaComision;

public interface PoolMonedaComisionRepository extends JpaRepository<PoolMonedaComision, Long> {

    @Query("""
            SELECT pmc FROM PoolMonedaComision pmc
            WHERE pmc.pool.id = :poolId
            AND LOWER(pmc.moneda) = LOWER(:moneda)
            """)
    Optional<PoolMonedaComision> findByPoolIdAndMoneda(
            @Param("poolId") Long poolId,
            @Param("moneda") String moneda);

    @Query("""
            SELECT DISTINCT pmc.moneda FROM PoolMonedaComision pmc
            WHERE pmc.pool.id = :poolId
            """)
    List<String> findMonedasByPoolId(@Param("poolId") Long poolId);

    @Query("""
            SELECT pmc.comision FROM PoolMonedaComision pmc
            WHERE pmc.pool.id = :poolId
            AND LOWER(pmc.moneda) = LOWER(:moneda)
            """)
    Optional<Double> findComisionByPoolIdAndMoneda(
            @Param("poolId") Long poolId,
            @Param("moneda") String moneda);
}
