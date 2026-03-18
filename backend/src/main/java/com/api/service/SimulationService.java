package com.api.service;

import com.api.session.SessionContext;
import com.api.ws.WebSocketBroadcaster;
import com.sim.config.WorldConfig;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SimulationService {

    private final Map<UUID, SessionContext> sessions = new ConcurrentHashMap<>();
    private final WebSocketBroadcaster broadcaster;

    public SimulationService(WebSocketBroadcaster broadcaster) {
        this.broadcaster = broadcaster;
    }

    public UUID createSession(WorldConfig config) {
        SessionContext session = new SessionContext(config, broadcaster);
        sessions.put(session.id(), session);
        return session.id();
    }

    public void start(UUID sessionId) {
        sessions.get(sessionId).start();
    }

    public void pause(UUID sessionId) {
        sessions.get(sessionId).pause();
    }

    public void resume(UUID sessionId) {
        sessions.get(sessionId).resume();
    }

    public void applySpeed(UUID sessionId, double speed) {
        sessions.get(sessionId).applySpeed(speed);
    }

    public void stop(UUID sessionId) {
        SessionContext session = sessions.remove(sessionId);
        if (session != null) session.stop();
    }

    public void sendPreview(UUID sessionId) {
        sessions.get(sessionId).sendInitialFrame();
    }

    public SessionContext get(UUID id) {
        return sessions.get(id);
    }

}