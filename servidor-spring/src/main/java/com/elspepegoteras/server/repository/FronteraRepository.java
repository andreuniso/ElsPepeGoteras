package com.elspepegoteras.server.repository;

import com.elspepegoteras.server.models.Frontera;
import com.elspepegoteras.server.models.Jugador;
import com.elspepegoteras.server.models.Pais;
import com.elspepegoteras.server.models.Partida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FronteraRepository extends JpaRepository<Frontera, Long> {
    @Query("SELECT f FROM Frontera f WHERE f.pais1 = :pais OR f.pais2 = :pais")
    List<Frontera> findFronterasByPais(@Param("pais") Pais pais);
    boolean existsByPais1AndPais2(Pais pais1, Pais pais2);
}
