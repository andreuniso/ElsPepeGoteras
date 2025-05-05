package com.elspepegoteras.server.models;

import java.util.ArrayList;
import java.util.List;

public class Jugador {
    private long id;
    private Usuari usuari;
    private Partida partida;
    private int numero;
    private List<Carta> cartes;

    //Creació d'un jugador
    public Jugador(Usuari usuari) {
        setUsuari(usuari);
        setCartes(new ArrayList<Carta>());
    }

    //Recuperació d'un jugador
    public Jugador(long id, Usuari usuari, Partida partida, int numero, List<Carta> cartes) {
        setId(id);
        setUsuari(usuari);
        setPartida(partida);
        setNumero(numero);
        setCartes(cartes);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Usuari getUsuari() {
        return usuari;
    }

    public void setUsuari(Usuari usuari) {
        this.usuari = usuari;
    }

    public Partida getPartida() {
        return partida;
    }

    public void setPartida(Partida partida) {
        this.partida = partida;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public List<Carta> getCartes() {
        return cartes;
    }

    public void setCartes(List<Carta> cartes) {
        this.cartes = cartes;
    }
}
