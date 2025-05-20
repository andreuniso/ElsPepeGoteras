package com.elspepegoteras.server.service;

import com.elspepegoteras.server.models.Jugador;
import com.elspepegoteras.server.models.Okupa;
import com.elspepegoteras.server.models.Pais;
import com.elspepegoteras.server.models.Partida;
import com.elspepegoteras.server.repository.OkupaRepository;
import org.springframework.stereotype.Service;

@Service
public class OkupaService {
    private final OkupaRepository okupaRepository;

    public OkupaService(OkupaRepository okupaRepository) {
        this.okupaRepository = okupaRepository;
    }

    public Okupa getOkupaByPaisAndPartida(Long idPais, Partida partida) {
        return okupaRepository.findByIdPaisAndJugador_Partida(idPais, partida).orElse(null);
    }

    public void guardarOkupa(Okupa okupa) {
        okupaRepository.save(okupa);
    }
}
