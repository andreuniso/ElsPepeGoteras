package com.elspepegoteras.server.database;

import com.elspepegoteras.server.interfaces.IRiskManager;
import com.elspepegoteras.server.interfaces.RiskManagerException;
import com.elspepegoteras.server.models.Jugador;
import com.elspepegoteras.server.models.Partida;
import com.elspepegoteras.server.models.Usuari;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

public class RiskManagerJDBC implements IRiskManager {

    @Override
    public boolean validarUsuari(String nickname, String password) throws RiskManagerException {
        return false;
    }

    @Override
    public long crearUsuari(String nom, String nickname, String password) throws RiskManagerException {
        return 0;
    }

    @Override
    public long crearPartida(String nom, String token, int maxJugadors, Jugador admin) throws RiskManagerException {
        return 0;
    }

    @Override
    public boolean actualitzarUsuari(Usuari usuari) throws RiskManagerException {
        return false;
    }

    @Override
    public boolean actualitzarPartida(Partida partida) throws RiskManagerException {
        return false;
    }

    @Override
    public boolean eliminarUsuari(long id) throws RiskManagerException {
        return false;
    }

    @Override
    public boolean eliminarPartida(long id) throws RiskManagerException {
        return false;
    }

    @Override
    public Usuari getUsuari(long id) throws RiskManagerException {
        return null;
    }

    @Override
    public byte[] getAvatarUsuari(Usuari usuari) throws RiskManagerException {
        return new byte[0];
    }

    @Override
    public Partida getPartida(long id) throws RiskManagerException {
        return null;
    }
}
