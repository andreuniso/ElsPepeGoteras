package com.elspepegoteras.server.models;

import java.util.Date;

import jakarta.persistence.*;

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

    @Column(name = "admin_id")
    private Long adminId;

    @Column (name = "torn_player_id")
    private Long tornPlayerId;

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
    public Partida(String nom, String token, int maxJugadors, Long adminId) {
        setNom(nom);
        setToken(token);
        setMaxJugadors(maxJugadors);
        setAdminId(adminId);
        setDataInici(new Date());
        setEstat(Estats.ESPERA);
    }

    //Recuperació d'una partida
    public Partida(long id, Date dataInici, String nom, String token, int maxJugadors, Long adminId, Long tornPlayerId, Estats estat) {
        setId(id);
        setDataInici(dataInici);
        setNom(nom);
        setToken(token);
        setMaxJugadors(maxJugadors);
        setAdminId(adminId);
        setTornPlayerId(tornPlayerId);
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

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public Long getTornPlayerId() {
        return tornPlayerId;
    }

    public void setTornPlayerId(Long tornPlayerId) {
        this.tornPlayerId = tornPlayerId;
    }

    public Estats getEstat() {
        return estat;
    }

    public void setEstat(Estats estat) {
        this.estat = estat;
    }
}
