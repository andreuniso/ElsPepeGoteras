package com.elspepegoteras.server.dto;

import com.elspepegoteras.server.models.Jugador;

public class PartidaDTO {
    private String nom;
    private String token;
    private int maxJugadors;
    private Jugador admin;

    public PartidaDTO(String nom, String token, int maxJugadors, Jugador admin) {
        this.nom = nom;
        this.token = token;
        this.maxJugadors = maxJugadors;
        this.admin = admin;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getMaxJugadors() {
        return maxJugadors;
    }

    public void setMaxJugadors(int maxJugadors) {
        this.maxJugadors = maxJugadors;
    }

    public Jugador getAdmin() {
        return admin;
    }

    public void setAdmin(Jugador admin) {
        this.admin = admin;
    }
}
