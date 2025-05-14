package com.elspepegoteras.server.models;

import jakarta.persistence.*;

@Entity
public class Continent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String nom;

    @Column(name = "reforc_tropes")
    private int reforcTropes;

    //Creació d'un continent
    public Continent(String nom, int reforcTropes) {
        setNom(nom);
        setReforcTropes(reforcTropes);
    }

    //Recuperació d'un continent
    public Continent(long id, String nom, int reforcTropes) {
        setId(id);
        setNom(nom);
        setReforcTropes(reforcTropes);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getReforcTropes() {
        return reforcTropes;
    }

    public void setReforcTropes(int reforcTropes) {
        this.reforcTropes = reforcTropes;
    }
}
   