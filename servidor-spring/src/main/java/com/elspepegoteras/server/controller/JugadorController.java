package com.elspepegoteras.server.controller;

import com.elspepegoteras.server.models.Jugador;
import com.elspepegoteras.server.service.JugadorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/jugador")
public class JugadorController {
    private final JugadorService jugadorService;

    public JugadorController(JugadorService jugadorService) {
        this.jugadorService = jugadorService;
    }

    //Recuperar jugador per ID
    @GetMapping("/id/{id}")
    public Jugador getJugadorById(@PathVariable long id) {
        return jugadorService.getJugadorById(id);
    }

    //Recuperar jugadors per partida
    @GetMapping("/partida/{idPartida}")
    public List<Jugador> getJugadorsByPartida(@PathVariable Long idPartida) {
        return jugadorService.getJugadorsByPartidaId(idPartida);
    }
}
