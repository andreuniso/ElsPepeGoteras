package com.elspepegoteras.server.models;

import java.util.Date;
import jakarta.persistence.*;

@Entity
public class Partida {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "data_inici", nullable = false)
    private Date dataInici;

    @Column(nullable = false)
    private String nom;

    @Column(unique = true)
    private String token;

    @Column(name = "max_players", nullable = false)
    private int maxJugadors;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Jugador admin;

    @ManyToOne
    @JoinColumn(name = "torn_player_id")
    private Jugador jugadorActual;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "estat_torn", nullable = false)
    private Estats estat;

    //Constructor per defecte
    public Partida() {}

    //Creació d'una partida
    public Partida(String nom, String token, int maxJugadors, Jugador admin) {
        setNom(nom);
        setToken(token);
        setMaxJugadors(maxJugadors);
        setAdmin(admin);
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
