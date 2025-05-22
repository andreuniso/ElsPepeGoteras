package com.elspepegoteras.server.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "jugador")
public class Jugador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "SKF_USER_ID", nullable = false)
    private Usuari usuari;

    @ManyToOne
    @JoinColumn(name = "SKF_PARTIDA_ID", nullable = false)
    private Partida partida;

    @Column(name = "SKF_NUMERO")
    private int numero;

    @Column(name = "AVAILABLE_TROOPS")
    private int tropes;

    @ManyToMany
    @JoinTable(
            name = "MA",
            joinColumns = @JoinColumn(name = "jugador_id"),
            inverseJoinColumns = @JoinColumn(name = "carta_id")
    )
    private List<Carta> cartes;

    //Constructor per defecte
    public Jugador() {
        setCartes(new ArrayList<Carta>());
        setTropes(0);
    }

    //Creació d'un jugador
    public Jugador(Usuari usuari) {
        setUsuari(usuari);
        setCartes(new ArrayList<Carta>());
        setTropes(0);
    }

    //Recuperació d'un jugador
    public Jugador(long id, Usuari usuari, Partida partida, int numero, List<Carta> cartes, int tropes) {
        setId(id);
        setUsuari(usuari);
        setPartida(partida);
        setNumero(numero);
        setCartes(cartes);
        setTropes(tropes);
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

    public int getTropes() {
        return tropes;
    }

    public void setTropes(int tropes) {
        this.tropes = tropes;
    }
}
