package com.api.resource.nodes.signal;

import com.api.resource.nodes.EditorNode;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

import static com.api.resource.ObjectCaster.getFloat;

public class ConstantValueNode extends EditorNode {

    private float value;

    @JsonProperty("data")
    public void unpackData(Map<String, Object> data) {
        this.value = getFloat(data, "value");
    }

    public float value() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
