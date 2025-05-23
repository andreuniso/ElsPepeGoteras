package com.elspepegoteras.server.websocket;

import com.elspepegoteras.server.models.*;
import com.elspepegoteras.server.service.*;
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
import java.util.stream.Collectors;

@Component
public class WebSocketHandler extends TextWebSocketHandler {
    private JugadorService jugadorService;
    private PartidaService partidaService;
    private PaisService paisService;
    private OkupaService okupaService;
    private FronteraService fronteraService;

    private final Map<Long, WebSocketSession> jugadorSessions = new ConcurrentHashMap<>();
    private final Map<Long, Set<WebSocketSession>> partidaSessions = new ConcurrentHashMap<>();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public WebSocketHandler(JugadorService jugadorService, PartidaService partidaService, PaisService paisService, OkupaService okupaService, FronteraService fronteraService) {
        this.jugadorService = jugadorService;
        this.partidaService = partidaService;
        this.paisService = paisService;
        this.okupaService = okupaService;
        this.fronteraService = fronteraService;
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
                case "REINFORCE_COUNTRIES" -> {
                    reinforceCountries(session, message);
                }
                case "ASSIGN_TROOPS" -> {
                    assignTroops(session, message);
                }
                case "ATTACK" -> {
                    attack(session, message);
                }
                case "FINISH_ATTACK" -> {
                    finish_attack(session, message);
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
     * Calcula el nombre de tropes disponibles per reforçar.
     *
     * @param jugador El jugador actual.
     * @param okupacions La llista d'okupacions del jugador.
     * @return El nombre de tropes disponibles.
     */
    private int calcularTropesDisponibles(Jugador jugador, List<Okupa> okupacions) {
        int territorisJugador = 0;
        Map<Long, Integer> paisosControlatsPerContinent = new HashMap<>();
        Map<Long, Integer> totalPaisosPerContinent = new HashMap<>();
        Map<Long, Integer> reforcPerContinent = new HashMap<>();

        for (Okupa ok : okupacions) {
            if (ok.getIdJugador() == jugador.getId()) {
                territorisJugador++;

                Pais pais = paisService.getPaisById(ok.getIdPais());
                if (pais != null && pais.getContinent() != null) {
                    long idContinent = pais.getContinent().getId();

                    //Comptar països controlats
                    paisosControlatsPerContinent.merge(idContinent, 1, Integer::sum);

                    //Comptar total països del continent si no estava ja comptat
                    totalPaisosPerContinent.putIfAbsent(idContinent,
                            (int) paisService.getAllPaises().stream()
                                    .filter(p -> p.getContinent().getId() == idContinent)
                                    .count());

                    //Guardar reforç per continent
                    reforcPerContinent.putIfAbsent(idContinent, pais.getContinent().getReforcTropes());
                }
            }
        }

        int reforcTerritoris = Math.max(3, territorisJugador / 3);
        int reforcContinents = 0;

        for (Long idContinent : paisosControlatsPerContinent.keySet()) {
            int controlats = paisosControlatsPerContinent.get(idContinent);
            int total = totalPaisosPerContinent.get(idContinent);
            if (controlats == total) {
                reforcContinents += reforcPerContinent.get(idContinent);
            }
        }

        return reforcTerritoris + reforcContinents;
    }

    /**
     * Tira un nombre determinat de daus i retorna els resultats.
     * @param qt El nombre de daus a tirar.
     * @return Una llista amb els resultats dels daus.
     */
    private List<Integer> tirarDaus(int qt) {
        Random random = new Random();
        List<Integer> resultats = new ArrayList<>();
        for (int i = 0; i < qt; i++) {
            resultats.add(random.nextInt(6) + 1); //1 a 6
        }
        return resultats;
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
                    if (partidaSessions.get(partida.getId()).size() == partida.getMaxJugadors()) {
                    //if (jugadors.size() == partida.getMaxJugadors()) {
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
                                //broadcastToPartida(partida.getId(), generarMissatgePartidaIniciada(partida));
                                broadcastToPartida(partida.getId(), generarMissatgeEstatPartida(partida));
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
            Long idPais = json.get("data").get("id_pais").asLong();

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
            Okupa okupaExist = okupaService.getOkupaByPaisAndPartida(pais.getId(), partida.getId());
            if (okupaExist != null) return;

            //Crear una ocupació nova
            Okupa okupa = new Okupa(pais.getId(), partida.getId(), jugador.getId(), 1);
            okupa = okupaService.guardarOkupa(okupa);

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
            .anyMatch(j -> okupaService.getOkupaByPaisAndPartida(p.getId(), partida.getId()) != null))
            .count();

            if (okupats == totsElsPaisos.size()) {
                partida.setEstat(Estats.REFORCAR_PAIS);
            }

            partidaService.actualizarPartida(partida);

            //Notifiquem a tothom
            //broadcastToPartida(partida.getId(), generarMissatgePaisActualitzat(okupa));
            //broadcastToPartida(partida.getId(), generarMissatgeNouTorn(partida));
            broadcastToPartida(partida.getId(), generarMissatgeEstatPartida(partida));
        } catch (Exception e) {
            System.out.println("❌ Error processant el missatge de col·locació de tropa: " + e.getMessage());
        }
    }

    /**
     * Reforça un país seleccionat pel jugador.
     *
     * @param session La sessió del WebSocket.
     * @param message El missatge rebut del client.
     */
    private void reinforceCountries(WebSocketSession session, TextMessage message) {
        int maxTropes;

        try {
            //Recuperem el jugador i la partida associada
            JsonNode json = objectMapper.readTree(message.getPayload());
            Jugador jugador = cercarJugador(session);
            Long idPais = json.get("data").get("id_pais").asLong();

            if (jugador == null) {
                System.out.println("❌ Jugador no trobat per id: " + jugador.getId());
                return;
            }

            //Busquem la partida associada al jugador
            Partida partida = jugador.getPartida();
            if (!partida.getEstat().equals(Estats.REFORCAR_PAIS)) return;

            if (partida.getMaxJugadors() == 3) {
                maxTropes = 35;
            } else if (partida.getMaxJugadors() == 4) {
                maxTropes = 30;
            } else if (partida.getMaxJugadors() == 5) {
                maxTropes = 25;
            } else {
                maxTropes = 30;
            }

            //Verifiquem que sigui el seu torn
            if (!partida.getTornPlayerId().equals(jugador.getId())) return;

            Pais pais = paisService.getPaisById(idPais);
            if (pais == null) return;

            //Verifiquem que el país té tropes (és a dir, té un Okupa associat) i que és propietat del jugador
            Okupa okupa = okupaService.getOkupaByPaisAndPartida(pais.getId(), partida.getId());
            if (okupa == null || okupa.getIdJugador() != jugador.getId()) return;

            //Afegir tropa a l'okupa
            okupa.setTropes(okupa.getTropes() + 1);
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

                boolean totesLesTropesColocades = true;
                for(Jugador j : jugadorService.getJugadorsByPartidaId(partida.getId())) {
                    List<Okupa> okupacions = okupaService.getAllByJugador(j.getId());
                    int totalTropes = okupacions.stream()
                    .mapToInt(Okupa::getTropes)
                    .sum();
                    if (totalTropes < maxTropes) {
                        totesLesTropesColocades = false;
                        break;
                    }
                }

                if (totesLesTropesColocades) {
                    List<Okupa> okupacions = okupaService.getAllByJugador(next.getId());
                    int tropesDisponibles = calcularTropesDisponibles(next, okupacions);

                    partida.setEstat(Estats.ASSIGNAR_TROPES);

                    next.setTropes(tropesDisponibles);
                    jugadorService.actualizarJugador(next);
                }

                partidaService.actualizarPartida(partida);
                broadcastToPartida(partida.getId(), generarMissatgeEstatPartida(partida));

                /*partidaService.actualizarPartida(partida);
                broadcastToPartida(partida.getId(), generarMissatgePaisActualitzat(okupa));
                broadcastToPartida(partida.getId(), generarMissatgeNouTorn(partida));

                //Enviem missatge al següent jugador amb el nombre de tropes disponibles
                if (totesLesTropesColocades) {
                    List<Okupa> okupacions = okupaService.getAllByJugador(next.getId());
                    int tropesDisponibles = calcularTropesDisponibles(next, okupacions);
                    broadcastToJugador(next.getId(), generarMissatgeTropesDisponibles(tropesDisponibles));
                }*/
            }
        } catch (Exception e) {
            System.out.println("❌ Error processant el missatge de reforç de països: " + e.getMessage());
        }
    }

    /**
     * Assigna tropes a un país seleccionat pel jugador.
     *
     * @param session La sessió del WebSocket.
     * @param message El missatge rebut del client.
     */
    private void assignTroops(WebSocketSession session, TextMessage message) {
        try {
            //Recuperem el jugador i la partida associada
            JsonNode json = objectMapper.readTree(message.getPayload());
            Jugador jugador = cercarJugador(session);
            Long idPais = json.get("data").get("id_pais").asLong();
            int qtTropes = json.get("data").get("qt_tropes").asInt();
            int qtTropesRestants = jugador.getTropes() - qtTropes;

            if (jugador == null) {
                System.out.println("❌ Jugador no trobat per id: " + jugador.getId());
                return;
            }

            //Busquem la partida associada al jugador
            Partida partida = jugador.getPartida();
            if (!partida.getEstat().equals(Estats.ASSIGNAR_TROPES)) return;

            //Verifiquem que sigui el seu torn
            if (!partida.getTornPlayerId().equals(jugador.getId())) return;

            Pais pais = paisService.getPaisById(idPais);
            if (pais == null) return;

            //Verifiquem que el país té tropes (és a dir, té un Okupa associat) i que és propietat del jugador
            Okupa okupa = okupaService.getOkupaByPaisAndPartida(pais.getId(), partida.getId());
            if (okupa == null || okupa.getIdJugador() != jugador.getId()) return;

            //Afegir tropa a l'okupa
            okupa.setTropes(okupa.getTropes() + qtTropes);
            jugador.setTropes(qtTropesRestants);
            okupaService.guardarOkupa(okupa);
            jugadorService.actualizarJugador(jugador);

            if (qtTropesRestants <= 0) {
                //Si no queden tropes per assignar, passem al següent estat
                partida.setEstat(Estats.ATAC);
                //broadcastToPartida(partida.getId(), generarMissatgeCanviEstat(partida));
            }

            partidaService.actualizarPartida(partida);
            broadcastToPartida(partida.getId(), generarMissatgeEstatPartida(partida));
            //broadcastToPartida(partida.getId(), generarMissatgePaisActualitzat(okupa));
        } catch (Exception e) {
            System.out.println("❌ Error processant el missatge d'assignació de tropes: " + e.getMessage());
        }
    }

    /**
     * Realitza un atac entre dos països seleccionats pel jugador.
     *
     * @param session La sessió del WebSocket.
     * @param message El missatge rebut del client.
     */
    private void attack(WebSocketSession session, TextMessage message) {
        try {
            JsonNode json = objectMapper.readTree(message.getPayload());
            Jugador jugador = cercarJugador(session);
            if (jugador == null) return;

            Long idPaisAtacant = json.get("data").get("id_pais_atacant").asLong();
            Long idPaisDefensiu = json.get("data").get("id_pais_defensiu").asLong();
            int tropesAtacant = json.get("data").get("qt_tropes_atacant").asInt();

            Partida partida = jugador.getPartida();
            if (!partida.getEstat().equals(Estats.ATAC)) return;
            if (!partida.getTornPlayerId().equals(jugador.getId())) return;

            Pais paisAtacant = paisService.getPaisById(idPaisAtacant);
            Pais paisDefensiu = paisService.getPaisById(idPaisDefensiu);
            if (paisAtacant == null || paisDefensiu == null) return;
            if (!fronteraService.sonFrontera(paisAtacant, paisDefensiu)) return;

            Okupa okAtacant = okupaService.getOkupaByPaisAndPartida(paisAtacant.getId(), partida.getId());
            Okupa okDefensor = okupaService.getOkupaByPaisAndPartida(paisDefensiu.getId(), partida.getId());
            if (okAtacant == null || okAtacant.getIdJugador() != jugador.getId()) return;
            if (okDefensor == null || okDefensor.getIdJugador() == jugador.getId()) return;
            if (okAtacant.getTropes() <= 1 || tropesAtacant >= okAtacant.getTropes()) return;

            //Configurar quants daus tira cada un
            int dauAtac = Math.min(tropesAtacant, 3);
            int dauDef = Math.min(okDefensor.getTropes(), 2);

            List<Integer> dausAtacant = tirarDaus(dauAtac);
            List<Integer> dausDefensor = tirarDaus(dauDef);

            //Ordenar descendent per comparar
            dausAtacant.sort(Comparator.reverseOrder());
            dausDefensor.sort(Comparator.reverseOrder());

            int baixesAtacant = 0;
            int baixesDefensor = 0;

            for (int i = 0; i < Math.min(dausAtacant.size(), dausDefensor.size()); i++) {
                if (dausAtacant.get(i) > dausDefensor.get(i)) {
                    baixesDefensor++;
                } else {
                    baixesAtacant++;
                }
            }

            //Actualitzar tropes
            okAtacant.setTropes(okAtacant.getTropes() - baixesAtacant);
            okDefensor.setTropes(okDefensor.getTropes() - baixesDefensor);

            okupaService.guardarOkupa(okAtacant);
            okupaService.guardarOkupa(okDefensor);

            //Conquesta
            if (okDefensor.getTropes() <= 0) {
                //Canviem l'okupa del país
                okDefensor.setIdJugador(jugador.getId());

                int tropesMoviment = tropesAtacant - baixesAtacant;
                if (tropesMoviment >= okAtacant.getTropes()) {
                    tropesMoviment = okAtacant.getTropes() - 1;
                }

                int tropesDesplasades = tropesMoviment / 2;
                okDefensor.setTropes(tropesDesplasades);
                okAtacant.setTropes(okAtacant.getTropes() - tropesDesplasades);

                okupaService.guardarOkupa(okDefensor);
                okupaService.guardarOkupa(okAtacant);

                //Comprovem si tot el mapa ha estat conquerit
                if (okupaService.getAllByJugador(jugador.getId()).size() == paisService.getAllPaises().size()) {
                    partida.setEstat(Estats.FINAL);
                } else {
                    partida.setEstat(Estats.FORTIFICACIO);
                }

                partidaService.actualizarPartida(partida);
            }

            broadcastToPartida(partida.getId(), generarMissatgeEstatPartida(partida, dausAtacant, dausDefensor));
        } catch (Exception e) {
            System.out.println("❌ Error processant atac: " + e.getMessage());
        }
    }

    /**
     * Finalitza l'atac i passa a la següent fase.
     *
     * @param session La sessió del WebSocket.
     * @param message El missatge rebut del client.
     */
    private void finish_attack(WebSocketSession session, TextMessage message) {
        try {
            Jugador jugador = cercarJugador(session);

            if (jugador == null) {
                System.out.println("❌ Jugador no trobat per id: " + jugador.getId());
                return;
            }

            //Busquem la partida associada al jugador
            Partida partida = jugador.getPartida();
            if (!partida.getEstat().equals(Estats.ATAC)) return;

            //Verifiquem que sigui el seu torn
            if (!partida.getTornPlayerId().equals(jugador.getId())) return;

            //Pas al estat de fortificació
            partida.setEstat(Estats.ASSIGNAR_TROPES);

            //Pas al següent jugador
            List<Jugador> jugadors = jugadorService.getJugadorsByPartidaId(partida.getId());
            int nextIndex = (jugador.getNumero() % jugadors.size()) + 1;
            Jugador next = jugadors.stream()
            .filter(j -> j.getNumero() == nextIndex)
            .findFirst()
            .orElse(null);

            if (next != null) {
                partida.setTornPlayerId(next.getId());

                List<Okupa> okupacions = okupaService.getAllByJugador(next.getId());
                int tropesDisponibles = calcularTropesDisponibles(next, okupacions);

                next.setTropes(tropesDisponibles);
                jugadorService.actualizarJugador(next);
            }

            partidaService.actualizarPartida(partida);

            broadcastToPartida(partida.getId(), generarMissatgeEstatPartida(partida));
        } catch (Exception e) {
            System.out.println("❌ Error processant el missatge de finalització d'atac: " + e.getMessage());
        }
    }

    /*****************************************GENERACIÓ DE MISSATGES JSON*****************************************/

    /**
     * Genera un missatge JSON per indicar que la partida ha estat iniciada.
     *
     * @param partida La partida iniciada.
     * @return El missatge JSON com a cadena de text.
     */
    /*private String generarMissatgePartidaIniciada(Partida partida) {
        ObjectNode root = objectMapper.createObjectNode();
        root.put("type", "game_started");
        root.set("data", objectMapper.valueToTree(partida));
        return root.toString();
    }*/

    /**
     * Genera un missatge JSON per indicar que un país ha estat actualitzat.
     *
     * @param okupa El país actualitzat.
     * @return El missatge JSON com a cadena de text.
     */
    /*private String generarMissatgePaisActualitzat(Okupa okupa) {
        ObjectNode root = objectMapper.createObjectNode();
        root.put("type", "country_updated");
        root.set("data", objectMapper.valueToTree(okupa));
        return root.toString();
    }*/

    /**
     * Genera un missatge JSON per indicar que és el torn d'un nou jugador.
     *
     * @param partida La partida actualitzada.
     * @return El missatge JSON com a cadena de text.
     */
    /*private String generarMissatgeNouTorn(Partida partida) {
        ObjectNode root = objectMapper.createObjectNode();
        root.put("type", "new_turn");
        root.set("data", objectMapper.valueToTree(partida));
        return root.toString();
    }*/

    /**
     * Genera un missatge JSON per indicar que ha canviat l'estat de la partida.
     *
     * @param partida La partida actualitzada.
     * @return El missatge JSON com a cadena de text.
     */
    /*private String generarMissatgeCanviEstat(Partida partida) {
        ObjectNode root = objectMapper.createObjectNode();
        root.put("type", "change_state");
        root.set("data", objectMapper.valueToTree(partida));
        return root.toString();
    }*/

    /**
     * Genera un missatge JSON per indicar que hi ha tropes disponibles per reforçar.
     *
     * @param tropesDisponibles El nombre de tropes disponibles.
     * @return El missatge JSON com a cadena de text.
     */
    /*private String generarMissatgeTropesDisponibles(int tropesDisponibles) {
        ObjectNode root = objectMapper.createObjectNode();
        root.put("type", "available_troops");

        ObjectNode data = objectMapper.createObjectNode();
        data.put("qt_tropes", tropesDisponibles);

        root.set("data", data);
        return root.toString();
    }*/

    /**
     * Genera un missatge JSON per indicar l'estat de la partida.
     * @param partida La partida actualitzada.
     * @return El missatge JSON com a cadena de text.
     */
    private String generarMissatgeEstatPartida(Partida partida, List<Integer> dausAtacant, List<Integer> dausDefensor) {
        ObjectNode root = objectMapper.createObjectNode();
        root.put("type", "game_state");

        ObjectNode dataNode = objectMapper.valueToTree(partida);

        Jugador jugadorActual = jugadorService.getJugadorById(partida.getTornPlayerId());
        dataNode.put("availableTroopsActualPlayer", jugadorActual.getTropes());

        //Si hi ha daus, afegir-los al missatge
        if (dausAtacant != null) {
            ArrayNode arrayDausAtacant = objectMapper.createArrayNode();
            dausAtacant.forEach(arrayDausAtacant::add);
            dataNode.set("dausAtacant", arrayDausAtacant);
        } else {
            dataNode.putNull("dausAtacant");
        }

        if (dausDefensor != null) {
            ArrayNode arrayDausDefensor = objectMapper.createArrayNode();
            dausDefensor.forEach(arrayDausDefensor::add);
            dataNode.set("dausDefensor", arrayDausDefensor);
        } else {
            dataNode.putNull("dausDefensor");
        }

        //Crear array de territoris
        ArrayNode territorisArray = objectMapper.createArrayNode();
        List<Pais> totsElsPaisos = paisService.getAllPaises();
        List<Okupa> okupes = okupaService.getOcupacionsByPartida(partida.getId());
        Map<Long, Okupa> mapaOkupes = okupes.stream().collect(Collectors.toMap(Okupa::getIdPais, o -> o));

        for (Pais pais : totsElsPaisos) {
            ObjectNode territoriNode = objectMapper.createObjectNode();
            Okupa o = mapaOkupes.get(pais.getId());

            territoriNode.put("idPais", pais.getId());

            if (o != null) {
                territoriNode.put("idJugador", o.getIdJugador());
                territoriNode.put("tropes", o.getTropes());
            } else {
                territoriNode.put("idJugador", 0);
                territoriNode.put("tropes", 0);
            }

            territorisArray.add(territoriNode);
        }

        dataNode.set("territories", territorisArray);
        root.set("data", dataNode);

        return root.toString();
    }

    private String generarMissatgeEstatPartida(Partida partida) {
        return generarMissatgeEstatPartida(partida, null, null);
    }

    /**************************************************BROADCAST**************************************************/

    /**
     * Envia un missatge a tots els jugadors de la partida.
     *
     * @param idPartida L'ID de la partida.
     * @param missatge  El missatge a enviar.
     */
    private void broadcastToPartida(Long idPartida, String missatge) {
        Set<WebSocketSession> sessions = partidaSessions.get(idPartida);
        if (sessions != null) {
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

    /**
     * Envia un missatge a un jugador específic.
     *
     * @param idJugador L'ID del jugador.
     * @param missatge  El missatge a enviar.
     */
    private void broadcastToJugador(Long idJugador, String missatge) {
        WebSocketSession session = jugadorSessions.get(idJugador);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(missatge));
            } catch (IOException e) {
                System.out.println("❌ Error enviant missatge al jugador " + idJugador + ": " + e.getMessage());
            }
        }
    }
}
