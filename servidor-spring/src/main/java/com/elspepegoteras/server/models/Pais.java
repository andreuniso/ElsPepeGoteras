package com.elspepegoteras.server.models;

import jakarta.persistence.*;

@Entity
@Table(name = "pais")
public class Pais {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nom;

    @ManyToOne
    @JoinColumn(name = "continent_id")
    private Continent continent;

    //Creació d'un país
    public Pais(String nom, Continent continent) {
        setNom(nom);
        setContinent(continent);
    }

    //Recuperació d'un país
    public Pais(long id, String nom, Continent continent) {
        setId(id);
        setNom(nom);
        setContinent(continent);
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

    public Continent getContinent() {
        return continent;
    }

    public void setContinent(Continent continent) {
        this.continent = continent;
    }
}
