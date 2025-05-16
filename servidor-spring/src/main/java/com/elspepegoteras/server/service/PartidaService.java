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

    /**
     * Recupera una partida per ID.
     * @param id L'ID de la partida a recuperar
     * @return Retorna un objecte Partida amb l'ID especificat, o null si no existeix
     */
    public Partida getPartidaById(long id) {
        return partidaRepository.findById(id);
    }

    /**
     * Recupera una partida per token.
     * @param token El token de la partida a recuperar
     * @return Retorna un objecte Partida amb el token especificat, o null si no existeix
     */
    public Partida getPartidaByToken(String token) {
        return partidaRepository.findByToken(token);
    }

    /**
     * Recupera una llista de partides públiques.
     * @return Retorna una llista d'objectes Partida que tenen el token a null
     */
    public List<Partida> getPartidesPubliques() {
        return partidaRepository.findByTokenIsNull();
    }

    /**
     * Crea una nova partida.
     * @param partidaDTO L'objecte PartidaDTO que conté la informació bàsica de la partida a crear
     * @return Retorna un objecte Partida amb la informació de la partida creada
     */
    public Partida crearPartida(PartidaDTO partidaDTO) {
        Usuari usuari = usuariRepository.findById(partidaDTO.getUserAdminId()).orElse(null);

        if (usuari != null) {
            //Generaci&oacute; d'un token &uacute;nic per la partida
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
            admin = jugadorRepository.save(admin);

            partida.setAdminId(admin.getId()); //Assignem el jugador admin a la partida
            partida = partidaRepository.save(partida); //Guardem la partida amb el jugador admin

            return partida;
        }

        return null;
    }

    /**
     * Actualitza una partida existent.
     * @param partida L'objecte Partida que conté la informació actualitzada de la partida
     * @return Retorna un objecte Partida amb la informació actualitzada
     */
    public Partida actualizarPartida(Partida partida) {
        return partidaRepository.save(partida);
    }

    /**
     * Elimina una partida i tots els jugadors associats.
     * @param id L'ID de la partida a eliminar
     */
    public void eliminarPartida(long id) {
        Partida partida = partidaRepository.findById(id);
        partida.setAdminId(null);
        partida.setTornPlayerId(null);
        partidaRepository.save(partida);

        List<Jugador> jugadors = jugadorRepository.findByPartida(partida);
        for (Jugador j : jugadors) {
            j.setPartida(null);
            jugadorRepository.delete(j);
        }

        partidaRepository.delete(partida);
    }
}
