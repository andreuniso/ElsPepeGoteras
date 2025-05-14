package com.elspepegoteras.server.repository;

import com.elspepegoteras.server.models.Jugador;
import com.elspepegoteras.server.models.Partida;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JugadorRepository extends JpaRepository<Jugador, Long> {
    Jugador findById(long id);
    List<Jugador> findByPartida(Partida partida);
}
