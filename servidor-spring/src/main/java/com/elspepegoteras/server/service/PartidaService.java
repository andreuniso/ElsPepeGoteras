package com.elspepegoteras.server.service;

import com.elspepegoteras.server.dto.PartidaDTO;
import com.elspepegoteras.server.models.Jugador;
import com.elspepegoteras.server.models.Partida;
import com.elspepegoteras.server.models.Usuari;
import com.elspepegoteras.server.repository.JugadorRepository;
import com.elspepegoteras.server.repository.PartidaRepository;
import com.elspepegoteras.server.repository.UsuariRepository;
import com.elspepegoteras.server.security.TokenGenerator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartidaService {

    private final PartidaRepository partidaRepository;
    private final JugadorRepository jugadorRepository;
    private final UsuariRepository usuariRepository;

    public PartidaService(PartidaRepository partidaRepository, JugadorRepository jugadorRepository, UsuariRepository usuariRepository) {
        this.partidaRepository = partidaRepository;
        this.jugadorRepository = jugadorRepository;
        this.usuariRepository = usuariRepository;
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
    public Partida crearPartida(PartidaDTO partidaDTO) {
        Usuari usuari = usuariRepository.findById(partidaDTO.getAdmin().getId()).orElse(null);

        if (usuari != null) {
            //Generació d'un token únic per la partida
            String token = null;
            if (partidaDTO.esPrivada()) {
                boolean tokenExists = true;

                //Generem tokens fins a trobar-ne un que no existeixi
                while (tokenExists) {
                    token = TokenGenerator.generateToken();
                    tokenExists = partidaRepository.existsByToken(token);
                }
            }

            Partida partida = new Partida(partidaDTO.getNom(), token, partidaDTO.getMaxJugadors());
            partida = partidaRepository.save(partida); //Guardem la partida a la BD

            Jugador admin = new Jugador(usuari); //Creem el jugador admin
            admin.setPartida(partida); //Assignem la partida al jugador

            jugadorRepository.save(admin); //Guardem el jugador a la BD

            partida.setAdmin(admin); //Assignem el jugador admin a la partida
            partida = partidaRepository.save(partida); //Guardem la partida amb el jugador admin

            return partida;
        }

        return null;
    }

    //Actualizar partida
    public Partida actualizarPartida(Partida partida) {
        return partidaRepository.save(partida);
    }

    //Eliminar partida
    public void eliminarPartida(long id) {
        Partida partida = partidaRepository.findById(id);

        //Eliminem la FK del jugador admin
        partida.setAdmin(null);
        partidaRepository.save(partida);

        List<Jugador> jugadors = jugadorRepository.findByPartida(partida);
        for (Jugador j : jugadors) {
            j.setPartida(null);
            jugadorRepository.delete(j);
        }

        partidaRepository.delete(partida);
    }
}
