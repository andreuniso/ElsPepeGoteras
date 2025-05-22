package com.elspepegoteras.server.service;

import com.elspepegoteras.server.repository.FronteraRepository;

public class FronteraService {
    private final FronteraRepository fronteraRepository;

    public FronteraService(FronteraRepository fronteraRepository) {
        this.fronteraRepository = fronteraRepository;
    }

    public boolean sonFrontera(Long id1, Long id2) {
        return fronteraRepository.existsByIdPais1AndIdPais2(id1, id2) ||
                fronteraRepository.existsByIdPais1AndIdPais2(id2, id1);
    }
}
