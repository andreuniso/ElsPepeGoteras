package com.elspepegoteras.server.repository;

import com.elspepegoteras.server.models.Partida;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PartidaRepository extends JpaRepository<Partida, Long> {
    Optional<Partida> findById(Long id);
    Optional<Partida> findByToken(String token);
    boolean existsByToken(String token);
    List<Partida> findByTokenIsNullAndTornPlayerIdIsNull(); //Partides públiques
}