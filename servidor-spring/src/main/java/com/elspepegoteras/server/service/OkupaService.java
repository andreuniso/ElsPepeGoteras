package com.elspepegoteras.server.service;

import com.elspepegoteras.server.models.Jugador;
import com.elspepegoteras.server.models.Okupa;
import com.elspepegoteras.server.models.Pais;
import com.elspepegoteras.server.repository.OkupaRepository;
import org.springframework.stereotype.Service;

@Service
public class OkupaService {
    private final OkupaRepository okupaRepository;

    public OkupaService(OkupaRepository okupaRepository) {
        this.okupaRepository = okupaRepository;
    }

    public Okupa getOkupaByPaisAndJugador(Long idPais, Jugador jugador) {
        return okupaRepository.findByIdPaisAndJugador(idPais, jugador).orElse(null);
    }

    public void guardarOkupa(Okupa okupa) {
        okupaRepository.save(okupa);
    }
}
