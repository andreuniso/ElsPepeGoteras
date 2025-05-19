package com.elspepegoteras.server.service;

import com.elspepegoteras.server.models.Jugador;
import com.elspepegoteras.server.repository.JugadorRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

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

    /**
     * Recupera una llista de jugadors associats a una partida.
     *
     * @param idPartida L'ID de la partida
     * @return Retorna una llista de jugadors associats a la partida especificada
     */
    @Transactional
    public List<Jugador> getJugadorsByPartida(Long idPartida) {
        List<Jugador> jugadores = jugadorRepository.findByPartidaId(idPartida);

        jugadores.forEach(j -> {
            j.getPaisosOkupats().size();
            j.getCartes().size();
        });

        return jugadores;
    }

    /**
     * Esborra un jugador per ID.
     *
     * @param id L'ID del jugador a esborrar
     */
    public void eliminarJugador(long id) {
        jugadorRepository.delete(jugadorRepository.findById(id));
    }
}
