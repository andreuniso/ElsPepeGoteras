package com.elspepegoteras.server.service;

import com.elspepegoteras.server.models.Usuari;
import com.elspepegoteras.server.repository.UsuariRepository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuariService {
    private final UsuariRepository usuariRepository;

    public UsuariService(UsuariRepository usuariRepository) {
        this.usuariRepository = usuariRepository;
    }

    public List<String> getAvatars() {
        try {
            URL resource = getClass().getClassLoader().getResource("static/avatars");
            if (resource == null) return Collections.emptyList();

            File avatarsFolder = new File(resource.toURI());
            File[] files = avatarsFolder.listFiles((dir, name) -> name.endsWith(".png"));

            if (files == null) return Collections.emptyList();

            return Arrays.stream(files)
            .map(File::getName)
            .collect(Collectors.toList());
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public Usuari login(String login, String password) {
        if (login == null || password == null) {
            return null;
        }

        Usuari usuari = usuariRepository.findByLogin(login);
        if (usuari != null && usuari.getPassword().equals(password)) {
            return usuari;
        }
        return null;
    }

    public Usuari register(String nom, String login, String password) {
        Usuari usuari = new Usuari(nom, login, password);
        return usuariRepository.save(usuari);
    }

    public Usuari actualitzarUsuari(Usuari usuari) {
        return usuariRepository.save(usuari);
    }

    public void eliminarUsuari(Long id) {
        usuariRepository.deleteById(id);
    }
}