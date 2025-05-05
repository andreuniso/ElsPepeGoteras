package com.elspepegoteras.server.models;

import jakarta.persistence.*;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Jugador {
    private WebSocketSession session;

    private long id;
    private Usuari usuari;
    private Partida partida;
    private int numero;
    private List<Carta> cartes;
    private List<Okupa> paisosOkupats;

    //Creació d'un jugador
    public Jugador(WebSocketSession session, Usuari usuari) {
        setSession(session);
        setUsuari(usuari);
        setCartes(new ArrayList<Carta>());
        setPaisosOkupats(new ArrayList<Okupa>());
    }

    //Recuperació d'un jugador
    public Jugador(long id, Usuari usuari, Partida partida, int numero, List<Carta> cartes, List<Okupa> paisosOkupats) {
        setId(id);
        setUsuari(usuari);
        setPartida(partida);
        setNumero(numero);
        setCartes(cartes);
        setPaisosOkupats(paisosOkupats);
    }

    public WebSocketSession getSession() {
        return session;
    }

    public void setSession(WebSocketSession session) {
        this.session = session;
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

    public List<Okupa> getPaisosOkupats() {
        return paisosOkupats;
    }

    public void setPaisosOkupats(List<Okupa> paisosOkupats) {
        this.paisosOkupats = paisosOkupats;
    }
}
