package com.api.ws;

import com.api.service.SimulationService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import java.util.UUID;

@Component
public class SimulationSocket extends BinaryWebSocketHandler {

    private final WebSocketBroadcaster broadcaster;
    private final SimulationService simulationService;

    public SimulationSocket(WebSocketBroadcaster broadcaster, SimulationService simulationService) {
        this.broadcaster = broadcaster;
        this.simulationService = simulationService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        broadcaster.add(session);

        UUID sessionId = UUID.fromString((String) session.getAttributes().get("sessionId"));

        simulationService.sendPreview(sessionId);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        broadcaster.remove(session);
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {

    }
}