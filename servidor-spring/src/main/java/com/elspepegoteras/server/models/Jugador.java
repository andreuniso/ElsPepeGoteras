package com.elspepegoteras.server.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Jugador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "skfUser_id")
    private Usuari usuari;

    @ManyToOne
    @JoinColumn(name = "skfPartida_id")
    private Partida partida;

    @Column(name = "skfNumero")
    private int numero;

    @OneToMany(mappedBy = "jugador")
    private List<Okupa> paisosOkupats;

    @ManyToMany
    @JoinTable(
            name = "MA",
            joinColumns = @JoinColumn(name = "jugador_id"),
            inverseJoinColumns = @JoinColumn(name = "carta_id")
    )
    private List<Carta> cartes;

    //Creació d'un jugador
    public Jugador(Usuari usuari) {
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
