package com.elspepegoteras.server.models;

import java.util.Date;

public class Partida {
    private long id;
    private Date dataInici;
    private String nom;
    private String token;
    private int maxJugadors;
    private Jugador admin;
    private Jugador jugadorActual;
    private Estats estat;

    //Creació d'una partida
    public Partida(String nom, String token, int maxJugadors) {
        setNom(nom);
        setToken(token);
        setMaxJugadors(maxJugadors);
        setDataInici(new Date());
        setEstat(Estats.ESPERA);
    }

    //Recuperació d'una partida
    public Partida(long id, Date dataInici, String nom, String token, int maxJugadors, Jugador admin, Jugador jugadorActual, Estats estat) {
        setId(id);
        setDataInici(dataInici);
        setNom(nom);
        setToken(token);
        setMaxJugadors(maxJugadors);
        setAdmin(admin);
        setJugadorActual(jugadorActual);
        setEstat(estat);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDataInici() {
        return dataInici;
    }

    public void setDataInici(Date dataInici) {
        this.dataInici = dataInici;
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

    public Jugador getJugadorActual() {
        return jugadorActual;
    }

    public void setJugadorActual(Jugador jugadorActual) {
        this.jugadorActual = jugadorActual;
    }

    public Estats getEstat() {
        return estat;
    }

    public void setEstat(Estats estat) {
        this.estat = estat;
    }
}
