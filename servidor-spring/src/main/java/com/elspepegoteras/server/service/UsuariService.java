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

    /**
     * Recupera una llista d'avatars disponibles.
     * @return Retorna una llista de noms d'avatars disponibles
     */
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

    /**
     * Recupera un usuari per ID.
     * @param login Nickname de l'usuari a recuperar
     * @param password La contrasenya de l'usuari a recuperar
     * @return Retorna un objecte Usuari si s'ha pogut verificar les credencials, o null si no existeix o les credencials són incorrectes
     */
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

    /**
     * Registra un nou usuari.
     * @param nom Nom de l'usuari a registrar
     * @param login Nickname de l'usuari a registrar
     * @param password La contrasenya de l'usuari a registrar
     * @return Retorna un objecte Usuari amb la informació de l'usuari registrat
     */
    public Usuari register(String nom, String login, String password) {
        Usuari usuari = new Usuari(nom, login, password);
        return usuariRepository.save(usuari);
    }

    /**
     * Actualitza un usuari.
     * @param usuari L'objecte Usuari amb la informació actualitzada
     * @return Retorna l'objecte Usuari actualitzat
     */
    public Usuari actualitzarUsuari(Usuari usuari) {
        return usuariRepository.save(usuari);
    }

    /**
     * Elimina un usuari per ID.
     * @param id L'ID de l'usuari a eliminar
     */
    public void eliminarUsuari(Long id) {
        usuariRepository.deleteById(id);
    }
}