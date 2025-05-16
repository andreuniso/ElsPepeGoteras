package com.elspepegoteras.server.service;

import com.elspepegoteras.server.models.Jugador;
import com.elspepegoteras.server.repository.JugadorRepository;
import org.springframework.stereotype.Service;

@Service
public class JugadorService {
    private final JugadorRepository jugadorRepository;

    public JugadorService(JugadorRepository jugadorRepository) {
        this.jugadorRepository = jugadorRepository;
    }

    /**
     * Recupera un jugador per ID.
     *
     * @param id L'ID del jugador a recuperar
     * @return Retorna un objecte Jugador amb l'ID especificat, o null si no existeix
     */
    public Jugador getJugadorById(long id) {
        return jugadorRepository.findById(id);
    }
}
