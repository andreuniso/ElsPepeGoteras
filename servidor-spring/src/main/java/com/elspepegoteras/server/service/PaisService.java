package com.elspepegoteras.server.service;

import com.elspepegoteras.server.models.Pais;
import com.elspepegoteras.server.repository.PaisRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PaisService {

    private final PaisRepository paisRepository;

    public PaisService(PaisRepository paisRepository) {
        this.paisRepository = paisRepository;
    }

    /**
     * Recupera un pais per ID.
     * @param id L'ID del pais a recuperar
     * @return Retorna un objecte Pais amb l'ID especificat, o null si no existeix
     */
    public Pais getPaisById(Long id) {
        return paisRepository.findById(id).orElse(null);
    }

    /**
     * Recupera tots els paisos disponibles.
     * @return Retorna una llista de tots els paisos disponibles
     */
    public List<Pais> getAllPaises() {
        return paisRepository.findAll();
    }
}
