package com.watts2crypto.watts2crypto_backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.watts2crypto.watts2crypto_backend.models.Pool;

public interface PoolRepository extends BaseRepository<Pool> {

    @Query("SELECT p FROM Pool p WHERE LOWER(p.nombre) = LOWER(:name)")
    Optional<Pool> findByName(@Param("name") String name);
}
