package com.elspepegoteras.server.service;

import com.elspepegoteras.server.models.Okupa;
import com.elspepegoteras.server.repository.OkupaRepository;
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

    /**
     * Guarda un Okupa a la base de dades.
     *
     * @param okupa Okupa a guardar.
     * @return Okupa guardat.
     */
    public Okupa guardarOkupa(Okupa okupa) {
        return okupaRepository.save(okupa);
    }

    /**
     * Agafa un llistat de països okupats per un jugador.
     * @param idJugador Jugador que ocupa els països.
     * @return Llistat de països okupats per un jugador.
     */
    public List<Okupa> getAllByJugador(Long idJugador) {
        return okupaRepository.findAllByIdJugador(idJugador);
    }

    /**
     * Agafa un llistat de països okupats per una partida.
     * @param id Partida que ocupa els països.
     * @return Llistat de països okupats per una partida.
     */
    public List<Okupa> getOcupacionsByPartida(long id) {
        return okupaRepository.findAllByIdPartida(id);
    }
}
