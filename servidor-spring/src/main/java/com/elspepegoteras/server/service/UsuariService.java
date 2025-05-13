package com.elspepegoteras.server.service;

import com.elspepegoteras.server.models.Usuari;
import com.elspepegoteras.server.repository.UsuariRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UsuariService {
    private final UsuariRepository usuariRepository;
    private final String avatarsDirectoryPath = "src/main/resources/static/avatars";
    private final Path avatarsDirectory = Paths.get(avatarsDirectoryPath);

    public UsuariService(UsuariRepository usuariRepository) {
        this.usuariRepository = usuariRepository;
    }

    public Usuari getUsuari(String login) {
        return usuariRepository.findByLogin(login);
    }

    public List<String> getAvatars() {
        try {
            // Carga la ruta como recurso desde el classpath
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

    public boolean login(String login, String password) {
        if (login == null || password == null) {
            return false;
        }

        Usuari usuari = usuariRepository.findByLogin(login);
        if (usuari != null) {
            return usuari.getPassword().equals(password);
        }
        return false;
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