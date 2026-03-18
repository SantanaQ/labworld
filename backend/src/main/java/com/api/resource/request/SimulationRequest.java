package com.api.resource.request;

import java.util.UUID;

public class SimulationRequest {

    UUID id;

    public UUID id() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
