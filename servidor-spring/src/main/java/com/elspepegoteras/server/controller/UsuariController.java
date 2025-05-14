package com.elspepegoteras.server.controller;

import com.elspepegoteras.server.dto.LoginDTO;
import com.elspepegoteras.server.dto.RegisterDTO;
import com.elspepegoteras.server.models.Usuari;
import com.elspepegoteras.server.service.UsuariService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuari")
public class UsuariController {
    private final UsuariService usuariService;

    public UsuariController(UsuariService usuariService) {
        this.usuariService = usuariService;
    }

    //Recuperar tots els avatares disponibles
    @GetMapping("/avatars")
    public List<String> getAvatars() {
        return usuariService.getAvatars();
    }

    //Validar credencials d'usuari
    @PostMapping("/login")
    public Usuari login(@RequestBody LoginDTO loginDTO) {
        return usuariService.login(loginDTO.getLogin(), loginDTO.getPassword());
    }

    //Registrar un nou usuari
    @PostMapping("/register")
    public Usuari register(@RequestBody RegisterDTO registerDTO) {
        try {
            return usuariService.register(registerDTO.getNom(), registerDTO.getLogin(), registerDTO.getPassword());
        } catch (Exception e) {
            e.printStackTrace(); // Te mostrará el error real en la consola
            throw e;
        }
    }

    //Actualitzar un usuari
    @PutMapping("/")
    public Usuari actualitzarUsuari(@RequestBody Usuari usuari) {
        return usuariService.actualitzarUsuari(usuari);
    }

    //Eliminar un usuari
    @DeleteMapping("/{id}")
    public void eliminarUsuari(@PathVariable Long id) {
        usuariService.eliminarUsuari(id);
    }
}
