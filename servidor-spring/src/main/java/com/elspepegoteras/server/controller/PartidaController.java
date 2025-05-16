package com.elspepegoteras.server.controller;

import com.elspepegoteras.server.dto.PartidaDTO;
import com.elspepegoteras.server.models.Partida;
import com.elspepegoteras.server.service.PartidaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/partida")
public class PartidaController {

    private final PartidaService partidaService;

    public PartidaController(PartidaService partidaService) {
        this.partidaService = partidaService;
    }

    //Recuperar partida per ID
    @GetMapping("/id/{id}")
    public Partida getPartidaById(@PathVariable long id) {
        return partidaService.getPartidaById(id);
    }

    //Recuperar partida per token
    @GetMapping("/token/{token}")
    public Partida getPartidaByToken(@PathVariable String token) {
        return partidaService.getPartidaByToken(token);
    }

    //Recuperar partides públiques
    @GetMapping("/public")
    public List<Partida> getPartidesPubliques() {
        return partidaService.getPartidesPubliques();
    }

    //Crear una nova partida
    @PostMapping("/crear")
    public Partida crearPartida(@RequestBody PartidaDTO partida) {
        return partidaService.crearPartida(partida);
    }

    //Actualitzar una partida
    @PutMapping("/actualitzar")
    public Partida actualizarPartida(@RequestBody Partida partida) {
        return partidaService.actualizarPartida(partida);
    }

    //Eliminar una partida
    @DeleteMapping("/eliminar/{id}")
    public void eliminarPartida(@PathVariable long id) {
        partidaService.eliminarPartida(id);
    }
}
