package com.elspepegoteras.server.service;

import com.elspepegoteras.server.models.Partida;
import com.elspepegoteras.server.repository.PartidaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartidaService {

    private final PartidaRepository partidaRepository;

    public PartidaService(PartidaRepository partidaRepository) {
        this.partidaRepository = partidaRepository;
    }

    //Recuperar partida per ID
    public Partida getPartidaById(long id) {
        return partidaRepository.findById(id);
    }

    //Recuperar partida per token
    public Partida getPartidaByToken(String token) {
        return partidaRepository.findByToken(token);
    }

    //Recuperar partides públiques
    public List<Partida> getPartidesPubliques() {
        return partidaRepository.findByTokenIsNull();
    }

    //Crear partida
    public Partida crearPartida(Partida partida) {
        return partidaRepository.save(partida);
    }

    //Actualizar partida
    public Partida actualizarPartida(Partida partida) {
        return partidaRepository.save(partida);
    }

    //Eliminar partida
    public void eliminarPartida(long id) {
        partidaRepository.deleteById(id);
    }
}
