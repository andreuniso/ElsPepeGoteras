package com.elspepegoteras.server.dto;

public class PartidaDTO {
    private String nom;
    private int maxJugadors;
    private boolean esPrivada;
    private long userAdminId;

    public PartidaDTO(String nom, int maxJugadors, boolean esPrivada, long userAdminId) {
        this.nom = nom;
        this.maxJugadors = maxJugadors;
        this.esPrivada = esPrivada;
        this.userAdminId = userAdminId;
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

    public long getUserAdminId() {
        return userAdminId;
    }

    public void setUserAdminId(long userAdminId) {
        this.userAdminId = userAdminId;
    }
}
