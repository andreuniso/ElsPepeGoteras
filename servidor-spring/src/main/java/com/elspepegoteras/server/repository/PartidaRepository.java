package com.elspepegoteras.server.repository;

import com.elspepegoteras.server.models.Partida;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartidaRepository extends JpaRepository<Partida, Long> {
    Partida findById(long id);
    Partida findByToken(String token);
    List<Partida> findByTokenIsNull(); //Partides públiques
}