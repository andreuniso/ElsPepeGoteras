package com.elspepegoteras.server.websocket;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WebSocketHandler extends TextWebSocketHandler {
    private List<WebSocketSession> sessions = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        //Afegir sessió a la llista
        sessions.add(session);
        System.out.println("Nova connexió: " + session.getId());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        //Gestió del missatge rebut
        System.out.println("Missatge rebut: " + message.getPayload());

        for (WebSocketSession s : sessions) {
            if (s.isOpen()) {
                try {
                    s.sendMessage(new TextMessage("Missatge rebut: " + message.getPayload()));
                } catch (IOException e) {
                    System.out.println("Error enviant el missatge. Més informació: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        //Eliminar sessió de la llista
        sessions.remove(session);
        System.out.println("Connexió tancada: " + session.getId());
    }
}
