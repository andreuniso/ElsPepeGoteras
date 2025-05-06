package com.elspepegoteras.server.websocket;

import com.elspepegoteras.server.database.RiskManagerJDBC;
import com.elspepegoteras.server.interfaces.IRiskManager;
import com.elspepegoteras.server.interfaces.RiskManagerException;
import com.elspepegoteras.server.managers.GameManager;
import com.elspepegoteras.server.models.Jugador;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebSocketHandler extends TextWebSocketHandler {
    //private static IRiskManager gBD = new RiskManagerJDBC();

    //private final GameManager gameManager = GameManager.getInstance();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        /*System.out.println("Nova connexió: " + session.getId());

        //Recollir paràmetres de la connexió
        Map<String, String> params = getQueryParams(session.getUri().getQuery());
        String idPartida = params.getOrDefault("idPartida",null);
        String idJugador = params.getOrDefault("idJugador", null);

        if (idPartida == null || idJugador == null) {
            //Si no s'han especificat idPartida o idJugador, no es pot afegir el jugador a la partida
            System.out.println("Error: idPartida o idJugador no especificats");
            return;
        } else {
            //Afegir el jugador a la partida
            try {
                gameManager.afegirJugadorAPartida(
                    Long.parseLong(idPartida),
                    new Jugador(session, gBD.getUsuari(Long.parseLong(idJugador)))
                );
            } catch (NumberFormatException e) {
                System.out.println("Error: idPartida o idJugador no son números vàlids");
                return;
            } catch (RiskManagerException e) {
                System.out.println("Error: No s'ha pogut obtenir l'usuari amb id " + idJugador);
            }
        }*/
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        //Gestió del missatge rebut
        System.out.println("Missatge rebut: " + message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        //Eliminar sessió de la llista
        //gameManager.eliminarJugador(session);
        System.out.println("Connexió tancada: " + session.getId());
    }

    private Map<String, String> getQueryParams(String query) {
        Map<String, String> map = new HashMap<>();
        if (query == null) return map;
        for (String param : query.split("&")) {
            String[] pair = param.split("=");
            if (pair.length == 2) {
                map.put(pair[0], pair[1]);
            }
        }
        return map;
    }
}
