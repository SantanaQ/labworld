package com.api.resource.nodes.layerStep;

import com.api.resource.nodes.EditorNode;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

import static com.api.resource.ObjectCaster.getFloat;
import static com.api.resource.ObjectCaster.getString;

public class SuitabilityDecayNode extends EditorNode {

    private String reference;
    private float min;
    private float max;
    private float decay;

    @JsonProperty("data")
    public void unpackData(Map<String, Object> data) {
        this.reference =  getString(data, "reference");
        this.min = getFloat(data, "min");
        this.max =  getFloat(data, "max");
        this.decay = getFloat(data, "decay");
    }


    public String reference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
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

    public float decay() {
        return decay;
    }

    public void setDecay(float decay) {
        this.decay = decay;
    }
}
