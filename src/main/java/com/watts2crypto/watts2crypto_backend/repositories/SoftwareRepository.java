package com.watts2crypto.watts2crypto_backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.watts2crypto.watts2crypto_backend.models.Software;

public interface SoftwareRepository extends BaseRepository<Software> {

    @Query("SELECT s FROM Software s WHERE LOWER(s.nombre) = lower(:name)")
    Optional<Software> findByName(@Param("name") String name);
}
