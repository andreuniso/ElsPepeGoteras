package com.elspepegoteras.server.repository;

import com.elspepegoteras.server.models.Pais;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface PaisRepository extends JpaRepository<Pais, Long> {
    Optional<Pais> findById(Long id);
}
