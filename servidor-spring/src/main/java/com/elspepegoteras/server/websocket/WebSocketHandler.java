package com.elspepegoteras.server.websocket;

import com.elspepegoteras.server.models.*;
import com.elspepegoteras.server.service.JugadorService;
import com.elspepegoteras.server.service.OkupaService;
import com.elspepegoteras.server.service.PaisService;
import com.elspepegoteras.server.service.PartidaService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketHandler extends TextWebSocketHandler {
    private JugadorService jugadorService;
    private PartidaService partidaService;
    private PaisService paisService;
    private OkupaService okupaService;

    private final Map<Long, WebSocketSession> jugadorSessions = new ConcurrentHashMap<>();
    private final Map<Long, Set<WebSocketSession>> partidaSessions = new ConcurrentHashMap<>();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public WebSocketHandler(JugadorService jugadorService, PartidaService partidaService, PaisService paisService, OkupaService okupaService) {
        this.jugadorService = jugadorService;
        this.partidaService = partidaService;
        this.paisService = paisService;
        this.okupaService = okupaService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        //Recuperem els paràmetres de la URL
        Map<String, String> params = getQueryParams(session.getUri().getQuery());
        Long idJugador = Long.parseLong(params.get("idJugador"));
        Long idPartida = Long.parseLong(params.get("idPartida"));

        //Comprovem que el jugador existeix
        Jugador jugador = jugadorService.getJugadorById(idJugador);
        if (jugador == null) {
            System.out.println("❌ Jugador no trobat: " + idJugador);
            return;
        }

        //Comprovem que la partida existeix
        Partida partida = partidaService.getPartidaById(idPartida);
        if (partida == null) {
            System.out.println("❌ Partida no trobada: " + idPartida);
            return;
        }

        if (partida.getEstat() == Estats.ESPERA) {
            //Guardem la sessió del jugador
            jugadorSessions.put(idJugador, session);

            //Guardem la sessió de la partida
            partidaSessions.putIfAbsent(idPartida, new HashSet<>());
            partidaSessions.get(idPartida).add(session);

            System.out.println("🔗 Usuari connectat: " + session.getId());

            actualitzarLlistaJugadors(idPartida);
        } else {
            System.out.println("❌ Partida no disponible per a l'usuari " + jugador.getUsuari().getLogin() + ". Estat actual: " + partida.getEstat());
            try {
                session.sendMessage(new TextMessage("{ \"error\": \"La partida ja ha estat iniciada!\" }"));
                session.close();
            } catch (IOException e) {
                System.out.println("❌ Error tancant sessió: " + e.getMessage());
            }
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            System.out.println("📩 Missatge rebut: " + message.getPayload());

            JsonNode json = objectMapper.readTree(message.getPayload());
            String type = json.get("type").asText();

            switch (type.toUpperCase()) {
                case "START_GAME" -> {
                    startGame(session, message);
                }
                case "PLACE_TROOP" -> {
                    placeTroop(session, message);
                }
                default -> {
                    try {
                        session.sendMessage(new TextMessage("{ \"error\": \"Comandament desconegut\" }"));
                    } catch (IOException e) {
                        System.out.println("❌ Error enviant missatge: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Error processant el missatge: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Error desconegut: " + e.getMessage());
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Jugador jugador = cercarJugador(session);
        if (jugador != null) {
            System.out.println("🔴 Jugador " + jugador.getUsuari().getLogin() + " desconnectat.");
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
                eliminarSession(session);

                //Sincrionització amb la BD
                jugadorService.eliminarJugador(jugador.getId());
            }

            //Actualitzem la llista de jugadors als clients
            actualitzarLlistaJugadors(jugador.getPartida().getId());
        } else {
            System.out.println("❌ No s'ha trobat el jugador associat a la sessió " + session.getId() + ". No es pot eliminar.");
        }
    }

    /*****************************************FUNCIONS AUXILIARS*****************************************/

    /**
     * Recupera els paràmetres de la URL i els retorna com un map.
     *
     * @param query La cadena de consulta de la URL.
     * @return Un mapa amb els paràmetres de la URL.
     */
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

    /**
     * Recupera el jugador associat a la sessió.
     *
     * @param session La sessió del WebSocket.
     * @return El jugador associat a la sessió, o null si no es troba.
     */
    private Jugador cercarJugador(WebSocketSession session) {
        for (Map.Entry<Long, WebSocketSession> entry : jugadorSessions.entrySet()) {
            if (entry.getValue().equals(session)) {
                return jugadorService.getJugadorById(entry.getKey());
            }
        }
        return null;
    }

    /**
     * Busca la partida associada a la sessió.
     *
     * @param session La sessió del WebSocket.
     * @return La partida associada a la sessió, o null si no es troba.
     */
    private Partida cercarPartida(WebSocketSession session) {
        for (Map.Entry<Long, Set<WebSocketSession>> entry : partidaSessions.entrySet()) {
            if (entry.getValue().contains(session)) {
                return partidaService.getPartidaById(entry.getKey());
            }
        }
        return null;
    }

    /**
     * Actualitza la llista de jugadors a tots els clients de la partida.
     *
     * @param idPartida L'ID de la partida.
     */
    private void actualitzarLlistaJugadors(Long idPartida) {
        Set<WebSocketSession> sessionsPartida = partidaSessions.get(idPartida);
        if (sessionsPartida == null) return;

        List<Jugador> jugadors = jugadorService.getJugadorsByPartidaId(idPartida);

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

    /**
     * Elimina la sessió del jugador de les dues llistes (jugadorSessions i partidaSessions).
     *
     * @param session La sessió del WebSocket a eliminar.
     */
    private void eliminarSession(WebSocketSession session) {
        //1. Buscar idJugador en jugadorSessions
        Jugador jugador = cercarJugador(session);

        if (jugador == null) {
            System.out.println("❌ Sessió no trobada a jugadorSessions");
            return;
        }

        //2. Esborrar la sessió de jugadorSessions
        jugadorSessions.remove(jugador.getId());

        //3. Esborrar la sessió del set en partidaSessions
        Long idPartida = jugador.getPartida().getId();
        Set<WebSocketSession> sessionsPartida = partidaSessions.get(idPartida);
        if (sessionsPartida != null) {
            sessionsPartida.remove(session);
            if (sessionsPartida.isEmpty()) {
                partidaSessions.remove(idPartida);
            }
        }

        System.out.println("🔴 Sessió eliminada pel jugador " + jugador.getUsuari().getLogin() + " a la partida " + jugador.getPartida().getNom());
    }

    /****************************************COMUNICACIÓ CLIENT/SERVIDOR****************************************/

    /**
     * Inicia la partida i envia un missatge a tots els jugadors per indicar que el joc ha començat.
     *
     * @param session La sessió del WebSocket.
     * @param message El missatge rebut del client.
     */
    private void startGame(WebSocketSession session, TextMessage message) {
        //Modifiquem la partida per indicar el torn
        try {
            Partida partida = cercarPartida(session);
            Jugador admin = cercarJugador(session);

            if (partida != null) {
                if (partida.getAdminId().equals(admin.getId())) {
                    //Buscar jugador amb número 1 a la partida
                    List<Jugador> jugadors = jugadorService.getJugadorsByPartidaId(partida.getId());
                    if (jugadors.size() == partida.getMaxJugadors()) {
                        if (jugadors != null && !jugadors.isEmpty()) {
                            Jugador primerJugador = jugadors.stream()
                                    .filter(j -> j.getNumero() == 1)
                                    .findFirst()
                                    .orElse(null);
                            if (primerJugador != null) {
                                partida.setEstat(Estats.COLOCACIO_INICIAL);
                                partida.setTornPlayerId(primerJugador.getId());
                                partidaService.actualizarPartida(partida);

                                //Enviem un missatge a tots els jugadors de la partida per iniciar el joc
                                broadcastToPartida(partida.getId(), generarMissatgePartidaIniciada(partida));
                            } else {
                                System.out.println("❌ No s'ha trobat cap jugador amb número 1 a la partida " + partida.getId());
                            }
                        } else {
                            System.out.println("❌ No s'ha trobat cap jugador a la partida " + partida.getId());
                        }
                    } else {
                        System.out.println("❌ La partida " + partida.getId() + " no té el nombre de jugadors correcte. Esperant a que s'uneixin més jugadors.");
                    }
                } else {
                    System.out.println("❌ Només l'administrador pot iniciar la partida.");
                }
            } else {
                System.out.println("❌ Partida no trobada per id: " + partida.getId());
            }
        } catch (Exception e) {
            System.out.println("❌ Error processant el missatge d'inici de partida: " + e.getMessage());
        }
    }

    /**
     * Col·loca una tropa en un país seleccionat pel jugador.
     *
     * @param session La sessió del WebSocket.
     * @param message El missatge rebut del client.
     */
    private void placeTroop(WebSocketSession session, TextMessage message) {
        try {
            //Recuperem el jugador i la partida associada
            JsonNode json = objectMapper.readTree(message.getPayload());
            Jugador jugador = cercarJugador(session);
            Long idPais = json.get("data").get("idPais").asLong();

            if (jugador == null) {
                System.out.println("❌ Jugador no trobat per id: " + jugador.getId());
                return;
            }

            //Busquem la partida associada al jugador
            Partida partida = jugador.getPartida();
            if (!partida.getEstat().equals(Estats.COLOCACIO_INICIAL)) return;

            //Verifiquem que sigui el seu torn
            if (!partida.getTornPlayerId().equals(jugador.getId())) return;

            Pais pais = paisService.getPaisById(idPais);
            if (pais == null) return;

            //Verifiquem que el país NO té tropes (és a dir, no hi ha cap Okupa associat)
            Okupa okupaExist = okupaService.getOkupaByPaisAndPartida(pais.getId(), jugador.getPartida());
            if (okupaExist != null) return;

            //Crear una ocupació nova
            Okupa okupa = new Okupa(pais.getId(), jugador, 1);
            okupaService.guardarOkupa(okupa);

            //Pas al següent jugador
            List<Jugador> jugadors = jugadorService.getJugadorsByPartidaId(partida.getId());
            int nextIndex = (jugador.getNumero() % jugadors.size()) + 1;

            Jugador next = jugadors.stream()
            .filter(j -> j.getNumero() == nextIndex)
            .findFirst()
            .orElse(null);

            if (next != null) {
                partida.setTornPlayerId(next.getId());
            }

            //Comprovem si tots els països tenen tropes (és a dir, tenen un okupa)
            List<Pais> totsElsPaisos = paisService.getAllPaises();
            long okupats = totsElsPaisos.stream()
            .filter(p -> jugadors.stream()
            .anyMatch(j -> okupaService.getOkupaByPaisAndPartida(p.getId(), partida) != null))
            .count();

            if (okupats == totsElsPaisos.size()) {
                partida.setEstat(Estats.REFORCAR_PAIS);
            }

            partidaService.actualizarPartida(partida);

            //Notifiquem a tothom
            broadcastToPartida(partida.getId(), generarMissatgePaisActualitzat(pais));
            broadcastToPartida(partida.getId(), generarMissatgeNouTorn(partida));
        } catch (Exception e) {
            System.out.println("❌ Error processant el missatge de col·locació de tropa: " + e.getMessage());
        }
    }

    /**
     * Genera un missatge JSON per indicar que la partida ha estat iniciada.
     *
     * @param partida La partida iniciada.
     * @return El missatge JSON com a cadena de text.
     */
    private String generarMissatgePartidaIniciada(Partida partida) {
        ObjectNode root = objectMapper.createObjectNode();
        root.put("type", "game_started");
        root.set("data", objectMapper.valueToTree(partida));
        return root.toString();
    }

    /**
     * Genera un missatge JSON per indicar que un país ha estat actualitzat.
     *
     * @param pais El país actualitzat.
     * @return El missatge JSON com a cadena de text.
     */
    private String generarMissatgePaisActualitzat(Pais pais) {
        ObjectNode root = objectMapper.createObjectNode();
        root.put("type", "country_updated");
        root.set("data", objectMapper.valueToTree(pais));
        return root.toString();
    }

    /**
     * Genera un missatge JSON per indicar que és el torn d'un nou jugador.
     *
     * @param partida La partida actualitzada.
     * @return El missatge JSON com a cadena de text.
     */
    private String generarMissatgeNouTorn(Partida partida) {
        ObjectNode root = objectMapper.createObjectNode();
        root.put("type", "new_turn");
        root.set("data", objectMapper.valueToTree(partida));
        return root.toString();
    }

    /**
     * Envia un missatge a tots els jugadors de la partida.
     *
     * @param idPartida L'ID de la partida.
     * @param missatge  El missatge a enviar.
     */
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
