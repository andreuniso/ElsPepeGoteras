package com.elspepegoteras.server.dto;

import java.util.List;

public class ContinentDTO {
    private long id;
    private String nom;
    private int reforcTropes;
    private List<PaisDTO> paisos;

    public ContinentDTO() {
    }

    public ContinentDTO(long id, String nom, int reforcTropes, List<PaisDTO> paisos) {
        this.id = id;
        this.nom = nom;
        this.reforcTropes = reforcTropes;
        this.paisos = paisos;
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

    public List<PaisDTO> getPaisos() {
        return paisos;
    }

    public void setPaisos(List<PaisDTO> paisos) {
        this.paisos = paisos;
    }
}
