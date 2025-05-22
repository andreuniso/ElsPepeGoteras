package com.elspepegoteras.server.repository;

import com.elspepegoteras.server.models.Continent;
import com.elspepegoteras.server.models.Jugador;
import com.elspepegoteras.server.models.Partida;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContinentRepository extends JpaRepository<Continent, Long> {
}
