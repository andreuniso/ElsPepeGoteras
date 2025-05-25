package com.elspepegoteras.server.repository;

import com.elspepegoteras.server.models.Okupa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OkupaRepository extends JpaRepository<Okupa, Long> {
    Optional<Okupa> findByIdPaisAndIdPartida(Long idPais, Long idPartida);
    List<Okupa> findAllByIdJugador(Long idJugador);
    List<Okupa> findAllByIdPartida(long id);
}
