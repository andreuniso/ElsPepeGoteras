package com.elspepegoteras.server.repository;

import com.elspepegoteras.server.models.Continent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContinentRepository extends JpaRepository<Continent, Long> {
}
