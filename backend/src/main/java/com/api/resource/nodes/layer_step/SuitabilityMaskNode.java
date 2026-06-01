package com.api.resource.nodes.layer_step;

import com.api.resource.nodes.EditorNode;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

import static com.api.resource.ObjectCaster.getFloat;
import static com.api.resource.ObjectCaster.getString;

public class SuitabilityMaskNode extends EditorNode {

    private String reference;
    private float min;
    private float max;

    @JsonProperty("data")
    public void unpackData(Map<String, Object> data) {
        this.reference =  getString(data, "reference");
        this.min = getFloat(data, "min");
        this.max =  getFloat(data, "max");
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

    public String reference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
