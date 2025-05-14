package com.elspepegoteras.server.models;

import java.io.Serializable;
import java.util.Objects;

public class FronteraId implements Serializable {
    private Long pais1;
    private Long pais2;

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