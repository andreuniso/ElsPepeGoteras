package com.elspepegoteras.server.models;

import jakarta.persistence.Column;

import java.io.Serializable;
import java.util.Objects;

public class OkupaId implements Serializable {
    @Column(name = "pais_id")
    private Long idPais;

    @Column(name = "skf_partida_id")
    private Long idPartida;

    public OkupaId() {}

    public OkupaId(Long idPais, Long idPartida) {
        this.idPais = idPais;
        this.idPartida = idPartida;
    }

    public Long getIdPais() {
        return idPais;
    }

    public void setIdPais(Long idPais) {
        this.idPais = idPais;
    }

    public Long getIdPartida() {
        return idPartida;
    }

    public void setIdPartida(Long idPartida) {
        this.idPartida = idPartida;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OkupaId okupaId = (OkupaId) o;
        return Objects.equals(idPais, okupaId.idPais) && Objects.equals(idPartida, okupaId.idPartida);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPais, idPartida);
    }
}