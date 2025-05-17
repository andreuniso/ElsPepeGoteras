package com.elspepegoteras.server.websocket;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketHandler extends TextWebSocketHandler {
    private final Map<Long, WebSocketSession> jugadorSessions = new ConcurrentHashMap<>();
    private final Map<Long, Set<WebSocketSession>> partidaSessions = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        //Recuperem els paràmetres de la URL
        Map<String, String> params = getQueryParams(session.getUri().getQuery());
        Long idJugador = Long.parseLong(params.get("idJugador"));
        Long idPartida = Long.parseLong(params.get("idPartida"));

        //Guardem la sessió del jugador
        jugadorSessions.put(idJugador, session);

        //Guardem la sessió de la partida
        partidaSessions.putIfAbsent(idPartida, new HashSet<>());
        partidaSessions.get(idPartida).add(session);

        //String userId = session.getId();
        //sessions.put(userId, session);
        System.out.println("🔗 Usuari connectat: " + session.getId());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        System.out.println("📩 Missatge rebut: " + message.getPayload());

        //Exemple de com enviar un missatge a tots els clients connectats
        for (WebSocketSession s : sessions.values()) {
            if (s.isOpen()) {
                try {
                    s.sendMessage(new TextMessage("🔄 Echo: " + message.getPayload()));
                } catch (IOException e) {
                    System.out.println("❌ Error enviant missatge: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session.getId());
        System.out.println("❌ Usuari desconnectat: " + session.getId());
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

    private void broadcastToPartida(Long idPartida, String missatge) {
        Set<WebSocketSession> sessions = partidaSessions.get(idPartida);
        if (sessions != null && !sessions.isEmpty() && missatge != null && !missatge.isEmpty() && idPartida != null && idPartida > 0) {
            for (WebSocketSession s : sessions) {
                if (s.isOpen()) {
                    try {
                        s.sendMessage(new TextMessage(missatge));
                    } catch (IOException e) {
                        System.out.println("❌ Error enviant missatge a la partida " + idPartida + ": " + e.getMessage());
                    }
                }
            }
        }
    }

}
