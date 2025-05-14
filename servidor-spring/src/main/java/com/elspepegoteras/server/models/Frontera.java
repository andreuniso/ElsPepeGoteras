package com.elspepegoteras.server.models;

import jakarta.persistence.*;

@Entity
@IdClass(FronteraId.class)
@Table(name = "frontera")
public class Frontera {
    @Id
    @ManyToOne
    @JoinColumn(name = "pais1_id")
    private Pais pais1;

    @Id
    @ManyToOne
    @JoinColumn(name = "pais2_id")
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
