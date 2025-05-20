package com.elspepegoteras.server.repository;

import com.elspepegoteras.server.models.Jugador;
import com.elspepegoteras.server.models.Okupa;
import com.elspepegoteras.server.models.Pais;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OkupaRepository extends JpaRepository<Okupa, Long> {
    Optional<Okupa> findByIdPaisAndJugador(Long idPais, Jugador jugador);
}
