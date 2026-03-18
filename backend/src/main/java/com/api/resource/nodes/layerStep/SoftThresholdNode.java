package com.api.resource.nodes.layerStep;

import com.api.resource.nodes.EditorNode;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sim.layer.step.SoftThreshold;

import java.util.Map;

import static com.api.resource.ObjectCaster.getFloat;

public class SoftThresholdNode extends EditorNode {

    private float threshold;
    private float softness;

    @JsonProperty("data")
    public void unpackData(Map<String, Object> data) {
        this.threshold =  getFloat(data, "threshold");
        this.softness = getFloat(data, "softness");
    }

    public float threshold() {
        return threshold;
    }

    public void setThreshold(float threshold) {
        this.threshold = threshold;
    }

    public float softness() {
        return softness;
    }

    public void setSoftness(float softness) {
        this.softness = softness;
    }
}
