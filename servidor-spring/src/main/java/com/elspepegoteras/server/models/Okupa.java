package com.elspepegoteras.server.models;

import jakarta.persistence.*;

@Entity
@Table(name = "okupa")
public class Okupa {
    @Id
    @Column(name = "pais_id")
    private Long idPais;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Jugador jugador;

    private int tropes;

    //Constructor per defecte
    public Okupa() {
    }

    //Creació - Recuperació d'un Okupa
    public Okupa(Long idPais, Jugador jugador, int tropes) {
        setIdPais(idPais);
        setJugador(jugador);
        setTropes(tropes);
    }

    public Long getIdPais() {
        return idPais;
    }

    public void setIdPais(Long idPais) {
        this.idPais = idPais;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public int getTropes() {
        return tropes;
    }

    public void setTropes(int tropes) {
        this.tropes = tropes;
    }
}
