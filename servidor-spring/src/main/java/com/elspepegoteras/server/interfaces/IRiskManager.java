package com.elspepegoteras.server.interfaces;

import com.elspepegoteras.server.models.Jugador;
import com.elspepegoteras.server.models.Partida;
import com.elspepegoteras.server.models.Usuari;

public interface IRiskManager {

    /************************************VALIDACIONS************************************/
    /**
     * Valida l'inici de sessió de l'usuari.
     * @param nickname Nom de l'usuari
     * @param password Contrasenya de l'usuari
     * @return true si l'usuari és vàlid, false en cas contrari
     * @throws RiskManagerException Excepció llançada en cas d'error
     */
    boolean validarUsuari(String nickname, String password) throws RiskManagerException;

    /**************************************INSERTS**************************************/
    /**
     * Registra un nou usuari al sistema.
     * @param nom Nom de l'usuari
     * @param nickname Nom d'usuari
     * @param password Contrasenya de l'usuari
     * @return ID de l'usuari creat o -1 si no s'ha pogut crear
     * @throws RiskManagerException Excepció llançada en cas d'error
     */
    long crearUsuari(String nom, String nickname, String password) throws RiskManagerException;

    /**
     * Crea una nova partida.
     * @param nom Nom de la partida
     * @param token Token de la partida
     * @param maxJugadors Màxim de jugadors
     * @param admin Jugador administrador
     * @return ID de la partida creada o -1 si no s'ha pogut crear
     * @throws RiskManagerException Excepció llançada en cas d'error
     */
    long crearPartida(String nom, String token, int maxJugadors, Jugador admin) throws RiskManagerException;

    /**************************************UPDATES**************************************/
    /**
     * Actualitza les dades d'un usuari.
     * @param usuari Usuari amb les dades actualitzades
     * @return true si l'usuari s'ha actualitzat correctament, false en cas contrari
     * @throws RiskManagerException Excepció llançada en cas d'error
     */
    boolean actualitzarUsuari(Usuari usuari) throws RiskManagerException;

    /**
     * Actualitza les dades d'una partida.
     * @param partida Partida amb les dades actualitzades
     * @return true si la partida s'ha actualitzat correctament, false en cas contrari
     * @throws RiskManagerException Excepció llançada en cas d'error
     */
    boolean actualitzarPartida(Partida partida) throws RiskManagerException;

    /**************************************DELETES**************************************/
    /**
     * Elimina un usuari del sistema.
     * @param id ID de l'usuari a eliminar
     * @return true si l'usuari s'ha eliminat correctament, false en cas contrari
     * @throws RiskManagerException Excepció llançada en cas d'error
     */
    boolean eliminarUsuari(long id) throws RiskManagerException;

    /**
     * Elimina una partida del sistema.
     * @param id ID de la partida a eliminar
     * @return true si la partida s'ha eliminat correctament, false en cas contrari
     * @throws RiskManagerException Excepció llançada en cas d'error
     */
    boolean eliminarPartida(long id) throws RiskManagerException;

    /**************************************SELECTS**************************************/
    /**
     * Obté les dades d'un usuari.
     * @param id ID de l'usuari
     * @return Usuari amb les dades sol·licitades
     * @throws RiskManagerException Excepció llançada en cas d'error
     */
    Usuari getUsuari(long id) throws RiskManagerException;

    /**
     * Obté l'avatar d'un usuari.
     * @param usuari Usuari del qual es vol obtenir l'avatar
     * @return byte[] amb la imatge d'usuari
     * @throws RiskManagerException Excepció llançada en cas d'error
     */
    byte[] getAvatarUsuari(Usuari usuari) throws RiskManagerException;

    /**
     * Obté les dades d'una partida.
     * @param id ID de la partida
     * @return Partida amb les dades sol·licitades
     * @throws RiskManagerException Excepció llançada en cas d'error
     */
    Partida getPartida(long id) throws RiskManagerException;
}
