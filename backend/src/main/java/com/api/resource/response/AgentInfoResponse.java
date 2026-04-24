package com.api.resource.response;

public record AgentInfoResponse(
        short id,
        float posX,
        float posY,
        float vX,
        float vY,
        float speed,
        float energy,
        float hunger,
        float heat,
        float curiosity,
        float fear
) {}
