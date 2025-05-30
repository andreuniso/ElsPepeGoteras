package com.elspepegoteras.server.models;

import jakarta.persistence.*;

@Entity
@IdClass(OkupaId.class)
@Table(name = "okupa")
public class Okupa {
    @Id
    @Column(name = "pais_id")
    private Long idPais;

    @Id
    @Column(name = "skf_partida_id")
    private Long idPartida;

    @Column(name = "player_id")
    private Long idJugador;

    private int tropes;

    //Constructor per defecte
    public Okupa() {
    }

    //Creació - Recuperació d'un Okupa
    public Okupa(Long idPais, Long idPartida, Long idJugador, int tropes) {
        setIdPais(idPais);
        setIdPartida(idPartida);
        setIdJugador(idJugador);
        setTropes(tropes);
    }

    public Long getIdPais() {
        return idPais;
    }

    public void setIdPais(Long idPais) {
        this.idPais = idPais;
    }

    public Long getIdPartida() {
        return idPartida;
    }

    public void setIdPartida(Long idPartida) {
        this.idPartida = idPartida;
    }

    public Long getIdJugador() {
        return idJugador;
    }

    public void setIdJugador(Long idJugador) {
        this.idJugador = idJugador;
    }

    public int getTropes() {
        return tropes;
    }

    public void setTropes(int tropes) {
        this.tropes = tropes;
    }
}
