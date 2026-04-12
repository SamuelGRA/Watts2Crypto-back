package com.watts2crypto.watts2crypto_backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.watts2crypto.watts2crypto_backend.models.SimbolosISOMonedas;

public interface SimbolosISOMonedasRepository extends BaseRepository<SimbolosISOMonedas> {

	@Query("SELECT s.simbolo FROM SimbolosISOMonedas s")
	List<String> findAllSymbols();
}
