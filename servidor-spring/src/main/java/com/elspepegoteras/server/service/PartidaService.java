package com.elspepegoteras.server.service;

import com.elspepegoteras.server.dto.JoinPartidaDTO;
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
import java.util.Objects;
import java.util.Optional;

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
        return partidaRepository.findById(id).orElse(null);
    }

    /**
     * Recupera una partida per token.
     * @param token El token de la partida a recuperar
     * @return Retorna un objecte Partida amb el token especificat, o null si no existeix
     */
    public Partida getPartidaByToken(String token) {
        return partidaRepository.findByToken(token).orElse(null);
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
     * S'uneix a una partida existent.
     * @param partida L'objecte JoinPartidaDTO que conté la informació de la partida a la qual s'unirà
     * @return Retorna un objecte Partida amb la informació de la partida unida
     */
    public Partida joinPartida(JoinPartidaDTO partida) {
        if (partida == null || (partida.getIdPartida() == null && partida.getToken() == null)) {
            return null; //No s'ha proporcionat cap identificador
        }

        Optional<Partida> partidaExistent = Optional.empty();
        if (partida.getIdPartida() != null) {
            //Partida pública
            partidaExistent = partidaRepository.findById(partida.getIdPartida());
        } else if (partida.getToken() != null) {
            //Partida privada
            partidaExistent = partidaRepository.findByToken(partida.getToken());
        }

        if (partidaExistent.isPresent()) {
            Partida p = partidaExistent.get();

            List<Jugador> jugadors = jugadorRepository.findByPartida(p);
            if (jugadors.size() >= p.getMaxJugadors()) {
                return null; //La partida ja està plena
            }

            Usuari usuari = usuariRepository.findById(partida.getIdUsuari()).orElse(null);
            if (usuari != null && jugadors.stream().anyMatch(j -> !Objects.equals(j.getUsuari().getId(), usuari.getId()))) {
                Jugador jugador = new Jugador(usuari);
                jugador.setPartida(p);
                jugadorRepository.save(jugador);

                return p;
            }
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
        Partida partida = partidaRepository.findById(id).orElse(null);
        if (partida != null) {
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
}
