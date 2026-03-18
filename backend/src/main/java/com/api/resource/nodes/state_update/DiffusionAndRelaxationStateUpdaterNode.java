package com.api.resource.nodes.state_update;

import com.api.resource.nodes.EditorNode;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

import static com.api.resource.ObjectCaster.getFloat;

public class DiffusionAndRelaxationStateUpdaterNode extends EditorNode {

    private float diffusion;
    private float relaxation;
    private float stateDecay;
    private float influenceDecay;

    @JsonProperty("data")
    public void unpackData(Map<String, Object> data) {
        this.diffusion = getFloat(data, "diffusion");
        this.relaxation = getFloat(data, "relaxation");
        this.stateDecay = getFloat(data, "stateDecay");
        this.influenceDecay = getFloat(data, "influenceDecay");
    }

    public float diffusion() {
        return diffusion;
    }

    public void setDiffusion(float diffusion) {
        this.diffusion = diffusion;
    }

    public float relaxation() {
        return relaxation;
    }

    public void setRelaxation(float relaxation) {
        this.relaxation = relaxation;
    }

    public float stateDecay() {
        return stateDecay;
    }

    public void setStateDecay(float stateDecay) {
        this.stateDecay = stateDecay;
    }

    public float influenceDecay() {
        return influenceDecay;
    }

    public void setInfluenceDecay(float influenceDecay) {
        this.influenceDecay = influenceDecay;
    }
}
