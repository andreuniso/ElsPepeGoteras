package com.elspepegoteras.server.models;

public class Carta {
    private long id;
    private TipusCarta tipusCarta;
    private Pais pais;

    //Creació d'una carta
    public Carta(TipusCarta tipusCarta, Pais pais) {
        setTipusCarta(tipusCarta);
        setPais(pais);
    }

    //Recuperació d'una carta
    public Carta(long id, TipusCarta tipusCarta, Pais pais) {
        setId(id);
        setTipusCarta(tipusCarta);
        setPais(pais);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public TipusCarta getTipusCarta() {
        return tipusCarta;
    }

    public void setTipusCarta(TipusCarta tipusCarta) {
        this.tipusCarta = tipusCarta;
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }
}
