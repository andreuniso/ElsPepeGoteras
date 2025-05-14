package com.elspepegoteras.server.models;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "partida")
public class Partida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @JsonBackReference
    private Jugador admin;

    @ManyToOne
    @JoinColumn(name = "torn_player_id")
    @JsonBackReference
    private Jugador jugadorActual;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "estat_torn", nullable = false)
    private Estats estat;

    //Constructor per defecte
    public Partida() {}

    //Creació d'una partida
    public Partida(String nom, String token, int maxJugadors) {
        setNom(nom);
        setToken(token);
        setMaxJugadors(maxJugadors);
        setDataInici(new Date());
        setEstat(Estats.ESPERA);
    }

    //Creació d'una partida
    public Partida(String nom, String token, int maxJugadors, Usuari admin) {
        setNom(nom);
        setToken(token);
        setMaxJugadors(maxJugadors);
        setAdmin(new Jugador(admin));
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
