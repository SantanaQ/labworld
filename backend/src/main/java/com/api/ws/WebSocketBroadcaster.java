package com.api.ws;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketBroadcaster {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final Map<String, Object> locks = new ConcurrentHashMap<>();

    public void add(WebSocketSession session) {
        String sessionId = getSessionId(session);

        WebSocketSession existing = sessions.put(sessionId, session);

        if (existing != null && existing.isOpen()) {
            try {
                existing.close();
                System.out.println("Closed duplicate session: " + sessionId);
            } catch (IOException ignored) {}
        }

        System.out.println("Connected: " + sessionId);
    }

    public void remove(WebSocketSession session) {
        String sessionId = getSessionId(session);

        sessions.remove(sessionId);
        locks.remove(sessionId);

        System.out.println("Disconnected: " + sessionId);
    }

    public void send(String sessionId, ByteBuffer data) {
        WebSocketSession session = sessions.get(sessionId);
        if (session == null || !session.isOpen()) return;

        try {
            ByteBuffer copy = ByteBuffer.allocate(data.remaining());
            copy.put(data.duplicate());
            copy.flip();



            session.sendMessage(new BinaryMessage(copy));

        } catch (IOException | IllegalStateException e) {
            System.out.println("Send failed: " + e.getMessage());
            remove(session);
        }
    }

    public void send(String sessionId, byte[] data) {
        WebSocketSession session = sessions.get(sessionId);
        if (session == null || !session.isOpen()) return;

        try {
            session.sendMessage(new BinaryMessage(data));

        } catch (IOException | IllegalStateException e) {
            System.out.println("Send failed: " + e.getMessage());
            e.printStackTrace();
            remove(session);
        }
    }

    private String getSessionId(WebSocketSession session) {
        return (String) session.getAttributes().get("sessionId");
    }
}