package com.elspepegoteras.server.models;

import com.elspepegoteras.server.service.FronteraService;
import com.elspepegoteras.server.service.PaisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class DadesGeografia {

    private final PaisService paisService;
    private final FronteraService fronteraService;

    private Map<Long, Pais> paisos;
    private Map<Long, List<Frontera>> fronteres;

    @Autowired
    public DadesGeografia(PaisService paisService, FronteraService fronteraService) {
        this.paisService = paisService;
        this.fronteraService = fronteraService;
        carregarDades();
    }

    private void carregarDades() {
        paisos = paisService.getAllPaises().stream()
                .collect(Collectors.toMap(Pais::getId, p -> p));

        fronteres = new HashMap<>();

        for (Pais pais : paisos.values()) {
            List<Frontera> llistatFronteres = fronteraService.getFronterasByPais(pais);
            fronteres.put(pais.getId(), llistatFronteres);
        }
    }

    public Pais getPais(Long id) {
        return paisos.get(id);
    }

    public Collection<Pais> getTotsElsPaisos() {
        return paisos.values();
    }

    public List<Frontera> getFronteres(Long idPais) {
        return fronteres.getOrDefault(idPais, Collections.emptyList());
    }
}

