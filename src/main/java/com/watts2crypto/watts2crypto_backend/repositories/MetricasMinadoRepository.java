package com.watts2crypto.watts2crypto_backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.watts2crypto.watts2crypto_backend.models.MetricasMinado;

public interface MetricasMinadoRepository extends BaseRepository<MetricasMinado> {

    @Query("SELECT mm FROM MetricasMinado mm WHERE mm.whatToMineCoinId = :coinId")
    Optional<MetricasMinado> findByWhatToMineCoinId(@Param("coinId") Integer coinId);

    @Query("SELECT mm FROM MetricasMinado mm WHERE LOWER(mm.nombreMoneda) = LOWER(:nombreMoneda)")
    Optional<MetricasMinado> findByNombreMonedaIgnoreCase(@Param("nombreMoneda") String nombreMoneda);

    @Query("SELECT mm.nombreMoneda FROM MetricasMinado mm")
    List<String> findAllNombresMonedasParaCalculo();
}
