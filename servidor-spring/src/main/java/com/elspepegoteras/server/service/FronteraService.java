package com.elspepegoteras.server.service;

import com.elspepegoteras.server.models.Frontera;
import com.elspepegoteras.server.models.Pais;
import com.elspepegoteras.server.repository.FronteraRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FronteraService {
    private final FronteraRepository fronteraRepository;

    public FronteraService(FronteraRepository fronteraRepository) {
        this.fronteraRepository = fronteraRepository;
    }

    /**
     * Revisa si dos països són frontera
     *
     * @param pais1 Pais 1
     * @param pais2 Pais 2
     * @return true si són frontera, false si no
     */
    public boolean sonFrontera(Pais pais1, Pais pais2) {
        return fronteraRepository.existsByPais1AndPais2(pais1, pais2) ||
                fronteraRepository.existsByPais1AndPais2(pais2, pais1);
    }

    /**
     * Recupera una llista de fronteres d'un país
     *
     * @param pais Pais del que es volen recuperar les fronteres
     * @return Llista de fronteres del país
     */
    public List<Frontera> getFronterasByPaisId(Pais pais) {
        return fronteraRepository.findFronterasByPais(pais);
    }
}
