package com.elspepegoteras.server.models;

public class Okupa {
    private Pais pais;
    private Jugador jugador;

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
