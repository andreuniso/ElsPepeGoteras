package com.elspepegoteras.server.service;

import com.elspepegoteras.server.models.Usuari;
import com.elspepegoteras.server.repository.UsuariRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuariService {
    private final UsuariRepository usuariRepository;

    @Autowired
    public UsuariService(UsuariRepository usuariRepository) {
        this.usuariRepository = usuariRepository;
    }

    public Usuari getUsuari(String login) {
        return usuariRepository.findByLogin(login);
    }

    public boolean login(String login, String password) {
        /*Usuari usuari = usuariRepository.findByLogin(login);
        if (usuari != null) {
            return usuari.getPassword().equals(password);
        }
        return false;*/

        if (login.equals("admin") && password.equals("admin")) {
            return true;
        } else {
            return false;
        }
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