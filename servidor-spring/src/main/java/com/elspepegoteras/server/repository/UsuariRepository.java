package com.elspepegoteras.server.repository;

import com.elspepegoteras.server.models.Usuari;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuariRepository extends JpaRepository<Usuari, Long> {
    Usuari findByLogin(String login);
}
