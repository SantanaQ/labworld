package com.api.resource.nodes.time_behavior;

import com.api.resource.nodes.EditorNode;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

import static com.api.resource.ObjectCaster.getFloat;

public class DriftNode extends EditorNode {

    private float speed;
    private float angle;

    @JsonProperty("data")
    public void unpackData(Map<String, Object> data) {
        this.speed = getFloat(data, "speed");
        this.angle = getFloat(data, "angle");
    }

    public float speed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float angle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
}
