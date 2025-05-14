package com.elspepegoteras.server.models;

import jakarta.persistence.*;

@Entity
@IdClass(OkupaId.class)
@Table(name = "okupa")
public class Okupa {
    @Id
    @ManyToOne
    @JoinColumn(name = "pais_id")
    private Pais pais;

    @Id
    @ManyToOne
    @JoinColumn(name = "player_id")
    private Jugador jugador;

    private int tropes;

    //Creació - Recuperació d'un Okupa
    public Okupa(Pais pais, Jugador jugador) {
        setPais(pais);
        setJugador(jugador);
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }
}
