package com.watts2crypto.watts2crypto_backend.repositories;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.watts2crypto.watts2crypto_backend.models.Criptomoneda;

public interface CriptomonedaRepository extends BaseRepository<Criptomoneda> {

    @Query("SELECT c FROM Criptomoneda c WHERE LOWER(c.nombre) = LOWER(:name)")
    Optional<Criptomoneda> findByNameIgnoreCase(@Param("name") String name);

    @Query("SELECT c FROM Criptomoneda c WHERE LOWER(c.assetId) = LOWER(:assetId)")
    Optional<Criptomoneda> findByAssetIdIgnoreCase(@Param("assetId") String assetId);

    @Query("SELECT c.nombre FROM Criptomoneda c")
    List<String> findAllNames();

    @Query("""
            SELECT p.precioEur FROM CriptomonedaPrecio p
            WHERE LOWER(p.criptomoneda.assetId) = LOWER(:assetId)
                AND p.fecha = (
                        SELECT MAX(p2.fecha) FROM CriptomonedaPrecio p2
                        WHERE LOWER(p2.criptomoneda.assetId) = LOWER(:assetId)
                )
            """)
    Optional<BigDecimal> findLatestPriceByAssetId(@Param("assetId") String assetId);

    @Query("SELECT c.simbolo FROM Criptomoneda c")
    List<String> findAllSymbols();

    @Query("SELECT c.assetId FROM Criptomoneda c WHERE LOWER(c.simbolo) = LOWER(:simbolo)")
    String findAssetIdBySymbol(@Param("simbolo") String simbolo);
}
