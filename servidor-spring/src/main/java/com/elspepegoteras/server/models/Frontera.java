package com.elspepegoteras.server.models;

public class Frontera {
    private Pais pais1;
    private Pais pais2;

    //Creació - Recuperació d'una frontera
    public Frontera(Pais pais1, Pais pais2) {
        setPais1(pais1);
        setPais2(pais2);
    }

    public Pais getPais1() {
        return pais1;
    }

    public void setPais1(Pais pais1) {
        this.pais1 = pais1;
    }

    public Pais getPais2() {
        return pais2;
    }

    public void setPais2(Pais pais2) {
        this.pais2 = pais2;
    }
}
