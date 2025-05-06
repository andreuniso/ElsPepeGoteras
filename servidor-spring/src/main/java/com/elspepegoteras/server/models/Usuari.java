package com.elspepegoteras.server.models;

import jakarta.persistence.*;

@Entity
public class Usuari {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;

    @Column(unique = true)
    private String login;
    private String password;
    private String avatar;
    private int wins;
    private int games;

    //Constructor per defecte
    public Usuari() {}

    //Creació d'un usuari
    public Usuari(String nom, String login, String password) {
        setNom(nom);
        setLogin(login);
        setPassword(password);
        setAvatar(null);
        setWins(0);
        setGames(0);
    }

    //Recuperació d'un usuari
    public Usuari(Long id, String nom, String login, String password, String avatar, Integer wins, Integer games) {
        setId(id);
        setNom(nom);
        setLogin(login);
        setPassword(password);
        setAvatar(avatar);
        setWins(wins);
        setGames(games);
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }
    public void setAvatar(String avatar) {
        if (avatar == null || avatar.isEmpty()) {
            this.avatar = "default_avatar.png";
        } else {
            this.avatar = avatar;
        }
    }

    public Integer getWins() {
        return wins;
    }
    public void setWins(Integer wins) {
        this.wins = wins;
    }

    public Integer getGames() {
        return games;
    }
    public void setGames(Integer games) {
        this.games = games;
    }
}
