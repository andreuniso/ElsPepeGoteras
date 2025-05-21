package com.elspepegoteras.server.service;

import com.elspepegoteras.server.models.Jugador;
import com.elspepegoteras.server.models.Okupa;
import com.elspepegoteras.server.models.Pais;
import com.elspepegoteras.server.models.Partida;
import com.elspepegoteras.server.repository.OkupaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OkupaService {
    private final OkupaRepository okupaRepository;

    public OkupaService(OkupaRepository okupaRepository) {
        this.okupaRepository = okupaRepository;
    }

    public Okupa getOkupaByPaisAndPartida(Long idPais, Long idPartida) {
        return okupaRepository.findByIdPaisAndIdPartida(idPais, idPartida).orElse(null);
    }

    public Okupa guardarOkupa(Okupa okupa) {
        return okupaRepository.save(okupa);
    }

    public List<Okupa> getAllByJugador(Long idJugador) {
        return okupaRepository.findAllByIdJugador(idJugador);
    }
}
