package com.api.resource.nodes.layerStep;

import com.api.resource.nodes.EditorNode;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

import static com.api.resource.ObjectCaster.getFloat;

public class NormalizeNode extends EditorNode {

    private float min;
    private float max;

    @JsonProperty("data")
    public void unpackData(Map<String, Object> data) {
        this.min =  getFloat(data, "min");
        this.max = getFloat(data, "max");
    }

    public float min() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public float max() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }
}
