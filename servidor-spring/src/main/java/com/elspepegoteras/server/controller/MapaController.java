package com.elspepegoteras.server.controller;

import com.elspepegoteras.server.dto.ContinentDTO;
import com.elspepegoteras.server.service.MapaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/mapa")
public class MapaController {
    private final MapaService mapaService;

    public MapaController(MapaService mapaService) {
        this.mapaService = mapaService;
    }

    //Recuperar el mapa
    @GetMapping
    public List<ContinentDTO> getMapa() {
        return mapaService.getMapa();
    }
}
