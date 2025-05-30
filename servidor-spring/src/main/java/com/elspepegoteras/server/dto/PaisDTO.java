package com.elspepegoteras.server.dto;

import java.util.List;

public class PaisDTO {
    private long id;
    private String nom;
    private List<Long> fronteres;

    public PaisDTO() {
    }

    public PaisDTO(long id, String nom, List<Long> fronteres) {
        this.id = id;
        this.nom = nom;
        this.fronteres = fronteres;
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

    public List<Long> getFronteres() {
        return fronteres;
    }

    public void setFronteres(List<Long> fronteres) {
        this.fronteres = fronteres;
    }
}