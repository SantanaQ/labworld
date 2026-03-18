package com.api.resource.nodes.layerStep;

import com.api.resource.nodes.EditorNode;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

import static com.api.resource.ObjectCaster.getFloat;

public class BinaryThresholdNode extends EditorNode{

    private float threshold;

    @JsonProperty("data")
    public void unpackData(Map<String, Object> data) {
        this.threshold =  getFloat(data, "threshold");
    }

    public float threshold() {
        return threshold;
    }

    public void setThreshold(float threshold) {
        this.threshold = threshold;
    }


}
