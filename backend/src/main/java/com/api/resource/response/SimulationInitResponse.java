package com.api.resource.response;

import com.sim.config.WorldConfig;

import java.util.UUID;

public class SimulationInitResponse {

    private final UUID sessionId;
    private final int width;
    private final int height;
    private final int layerCount;
    private final int agentCount;

    public SimulationInitResponse(UUID sessionId, WorldConfig config) {
        this.sessionId = sessionId;
        this.width = config.width();
        this.height = config.height();
        this.layerCount = config.layerCount();
        this.agentCount = config.agentCount();
    }

    public UUID getSessionId() { return sessionId; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getLayerCount() { return layerCount; }
    public int getAgentCount() { return agentCount; }
}