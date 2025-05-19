package com.elspepegoteras.server.websocket;

import com.elspepegoteras.server.models.Jugador;
import com.elspepegoteras.server.service.JugadorService;
import com.elspepegoteras.server.service.PartidaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketHandler extends TextWebSocketHandler {
    @Autowired
    private JugadorService jugadorService;

    @Autowired
    private PartidaService partidaService;

    private final Map<Long, WebSocketSession> jugadorSessions = new ConcurrentHashMap<>();
    private final Map<Long, Set<WebSocketSession>> partidaSessions = new ConcurrentHashMap<>();

    private final ObjectMapper objectMapper = new ObjectMapper();

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
        /*for (WebSocketSession s : sessions.values()) {
            if (s.isOpen()) {
                try {
                    s.sendMessage(new TextMessage("🔄 Echo: " + message.getPayload()));
                } catch (IOException e) {
                    System.out.println("❌ Error enviant missatge: " + e.getMessage());
                }
            }
        }*/
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {

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

    private void eliminarSession(WebSocketSession session) {
        //1. Buscar idJugador en jugadorSessions
        Long idJugador = null;
        for (Map.Entry<Long, WebSocketSession> entry : jugadorSessions.entrySet()) {
            if (entry.getValue().equals(session)) {
                idJugador = entry.getKey();
                break;
            }
        }

        if (idJugador == null) {
            System.out.println("❌ Sessió no trobada a jugadorSessions");
            return;
        }

        //2. Esborrar la sessió de jugadorSessions
        jugadorSessions.remove(idJugador);

        //3. Recuperar jugador per saber el idPartida
        Jugador jugador = jugadorService.getJugadorById(idJugador);
        if (jugador == null) {
            System.out.println("❌ Jugador no trobat per id: " + idJugador);
            return;
        }

        Long idPartida = jugador.getPartida().getId();

        //4. Esborrar la sessió del set en partidaSessions
        Set<WebSocketSession> sessionsPartida = partidaSessions.get(idPartida);
        if (sessionsPartida != null) {
            sessionsPartida.remove(session);
            if (sessionsPartida.isEmpty()) {
                partidaSessions.remove(idPartida);
            }
        }

        System.out.println("🔴 Sessió eliminada pel jugador " + idJugador + " a la partida " + idPartida);
    }


    private void jugadorLeft(WebSocketSession session) {
        //Recuperem el ID del jugador associat a la sessió
        Long key = null;
        for (Map.Entry<Long, WebSocketSession> entry : jugadorSessions.entrySet()) {
            if (entry.getValue().equals(session)) {
                key = entry.getKey();
                break;
            }
        }

        if (key != null) {
            System.out.println("🔴 Jugador " + key + " desconnectat.");

            Jugador jugador = jugadorService.getJugadorById(key); //Recuperem el jugador associat a la sessió
            if (jugador != null) {
                if (jugador.getPartida().getAdminId() == jugador.getId()) {
                    //Si el jugador era l'administrador, eliminem la partida
                    for(WebSocketSession s : partidaSessions.get(jugador.getPartida().getId())) {
                        if (s.isOpen()) {
                            try {
                                s.close();
                            } catch (IOException e) {
                                System.out.println("❌ Error tancant sessió: " + e.getMessage());
                            }
                        }
                        eliminarSession(s);
                    }

                    //Sincrionització amb la BD
                    partidaService.eliminarPartida(jugador.getPartida().getId());
                } else {
                    //Si el jugador no era l'administrador, actualitzem la llista de jugadors
                    if (session.isOpen()) {
                        try {
                            session.close();
                        } catch (IOException e) {
                            System.out.println("❌ Error tancant sessió: " + e.getMessage());
                        }
                    }
                    eliminarSession(session);

                    //Sincrionització amb la BD
                    jugadorService.eliminarJugador(jugador.getId());
                }

                //Actualitzem la llista de jugadors als clients
                actualitzarLlistaJugadors(jugador.getPartida().getId());
            } else {
                System.out.println("❌ No s'ha trobat el jugador associat a la sessió " + session.getId() + ". No es pot eliminar.");
            }

        } else {
            System.out.println("❌ No s'ha trobat el jugador associat a la sessió " + session.getId() + ". No es pot eliminar.");
        }
    }

    private void actualitzarLlistaJugadors(Long idPartida) {
        Set<WebSocketSession> sessionsPartida = partidaSessions.get(idPartida);
        if (sessionsPartida == null) return;

        List<Jugador> jugadors = jugadorService.getJugadorsByPartida(idPartida);

        try {
            ObjectNode root = objectMapper.createObjectNode();
            root.put("type", "reload_players");

            ObjectNode data = objectMapper.createObjectNode();
            ArrayNode jugadorsArray = objectMapper.createArrayNode();

            for (Jugador j : jugadors) {
                ObjectNode jugadorNode = objectMapper.valueToTree(j);
                jugadorsArray.add(jugadorNode);
            }

            data.set("jugadors", jugadorsArray);
            root.set("data", data);

            String missatge = objectMapper.writeValueAsString(root);

            broadcastToPartida(idPartida, missatge);

        } catch (Exception e) {
            System.out.println("❌ Error creant missatge JSON llista jugadors: " + e.getMessage());
        }
    }




}
