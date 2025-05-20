package com.elspepegoteras.server.models;

import java.io.Serializable;
import java.util.Objects;

public class FronteraId implements Serializable {
    private Long pais1;
    private Long pais2;

    //Constructor per defecte
    public FronteraId() {
    }

    //Creació d'una frontera
    public FronteraId(Long pais1, Long pais2) {
        setPais1(pais1);
        setPais2(pais2);
    }

    public Long getPais1() {
        return pais1;
    }

    public void setPais1(Long pais1) {
        this.pais1 = pais1;
    }

    public Long getPais2() {
        return pais2;
    }

    public void setPais2(Long pais2) {
        this.pais2 = pais2;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FronteraId that = (FronteraId) o;
        return Objects.equals(pais1, that.pais1) && Objects.equals(pais2, that.pais2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pais1, pais2);
    }
}