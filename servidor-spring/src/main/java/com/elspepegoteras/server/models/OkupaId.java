package com.elspepegoteras.server.models;

import java.io.Serializable;
import java.util.Objects;

public class OkupaId implements Serializable {
    private Long pais;
    private Long jugador;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OkupaId okupaId = (OkupaId) o;
        return Objects.equals(pais, okupaId.pais) && Objects.equals(jugador, okupaId.jugador);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pais, jugador);
    }
}
