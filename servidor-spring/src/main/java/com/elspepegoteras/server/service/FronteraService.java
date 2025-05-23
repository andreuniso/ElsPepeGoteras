package com.elspepegoteras.server.service;

import com.elspepegoteras.server.models.Pais;
import com.elspepegoteras.server.repository.FronteraRepository;
import org.springframework.stereotype.Service;

@Service
public class FronteraService {
    private final FronteraRepository fronteraRepository;

    public FronteraService(FronteraRepository fronteraRepository) {
        this.fronteraRepository = fronteraRepository;
    }

    public boolean sonFrontera(Pais pais1, Pais pais2) {
        return fronteraRepository.existsByPais1AndPais2(pais1, pais2) ||
                fronteraRepository.existsByPais1AndPais2(pais2, pais1);
    }
}
