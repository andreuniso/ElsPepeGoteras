package com.elspepegoteras.server.service;

import com.elspepegoteras.server.dto.ContinentDTO;
import com.elspepegoteras.server.dto.PaisDTO;
import com.elspepegoteras.server.models.Continent;
import com.elspepegoteras.server.repository.ContinentRepository;
import com.elspepegoteras.server.repository.FronteraRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MapaService {

    private ContinentRepository continentRepository;
    private FronteraRepository fronteraRepository;

    public MapaService(ContinentRepository continentRepository, FronteraRepository fronteraRepository) {
        this.continentRepository = continentRepository;
        this.fronteraRepository = fronteraRepository;
    }

    /**
     * Agafa el mapa de continents, paisos i les seves fronteres
     * @return Llista de continents amb els seus paisos i fronteres
     */
    public List<ContinentDTO> getMapa() {
        List<Continent> continents = continentRepository.findAll();

        return continents.stream().map(continent -> {
            ContinentDTO dto = new ContinentDTO();
            dto.setId(continent.getId());
            dto.setNom(continent.getNom());
            dto.setReforcTropes(continent.getReforcTropes());

            List<PaisDTO> paisos = continent.getPaisos().stream().map(pais -> {
                PaisDTO pdto = new PaisDTO();
                pdto.setId(pais.getId());
                pdto.setNom(pais.getNom());

                //Buscar fronteres
                List<Long> fronteres = fronteraRepository.findFronterasByPais(pais).stream()
                .map(f -> {
                    if (f.getPais1().getId() == pais.getId()) {
                        return f.getPais2().getId();
                    } else {
                        return f.getPais1().getId();
                    }
                })
                .collect(Collectors.toList());

                pdto.setFronteres(fronteres);
                return pdto;
            }).collect(Collectors.toList());

            dto.setPaisos(paisos);
            return dto;
        }).collect(Collectors.toList());
    }
}

