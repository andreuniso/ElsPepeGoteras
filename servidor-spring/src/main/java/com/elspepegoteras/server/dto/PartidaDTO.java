package com.elspepegoteras.server.dto;

import com.elspepegoteras.server.models.Usuari;

public class PartidaDTO {
    private String nom;
    private int maxJugadors;
    private boolean esPrivada;
    private Usuari admin;

    public PartidaDTO(String nom, int maxJugadors, boolean esPrivada, Usuari admin) {
        this.nom = nom;
        this.maxJugadors = maxJugadors;
        this.esPrivada = esPrivada;
        this.admin = admin;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getMaxJugadors() {
        return maxJugadors;
    }

    public void setMaxJugadors(int maxJugadors) {
        this.maxJugadors = maxJugadors;
    }

    public boolean esPrivada() {
        return esPrivada;
    }

    public void setPrivada(boolean esPrivada) {
        this.esPrivada = esPrivada;
    }

    public Usuari getAdmin() {
        return admin;
    }

    public void setAdmin(Usuari admin) {
        this.admin = admin;
    }
}
