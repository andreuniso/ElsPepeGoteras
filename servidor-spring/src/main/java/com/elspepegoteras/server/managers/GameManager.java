package com.elspepegoteras.server.managers;

import com.elspepegoteras.server.database.RiskManagerJDBC;
import com.elspepegoteras.server.interfaces.IRiskManager;
import com.elspepegoteras.server.interfaces.RiskManagerException;
import com.elspepegoteras.server.models.Jugador;
import com.elspepegoteras.server.models.Partida;
import com.elspepegoteras.server.models.Usuari;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class GameManager {
    private final List<Partida> games = new ArrayList<>();

    private static final GameManager instance = new GameManager();
    public static GameManager getInstance() {
        return instance;
    }

    public void afegirJugadorAPartida(long idPartida, Jugador jugador) {
        System.out.println("Afegint jugador (" + jugador.getUsuari().getNom() + ") a la partida " + idPartida);
        Partida partida = games.stream().filter(p -> p.getId() == idPartida).findFirst().orElse(null);

        if (partida != null) {
            //La partida ja existeix, afegim el jugador
            partida.addJugador(jugador);
        } else {
            //Creem una nova partida i afegim el jugador com administrador
        }
    }
}
