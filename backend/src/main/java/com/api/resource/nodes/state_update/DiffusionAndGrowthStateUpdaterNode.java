package com.api.resource.nodes.state_update;

import com.api.resource.nodes.EditorNode;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

import static com.api.resource.ObjectCaster.getFloat;

public class DiffusionAndGrowthStateUpdaterNode extends EditorNode {

    private float diffusion;
    private float growth;
    private float stateDecay;
    private float influenceDecay;

    @JsonProperty("data")
    public void unpackData(Map<String, Object> data) {
        this.diffusion = getFloat(data, "diffusion");
        this.growth = getFloat(data, "growth");
        this.stateDecay = getFloat(data, "stateDecay");
        this.influenceDecay = getFloat(data, "influenceDecay");
    }

    public float diffusion() {
        return diffusion;
    }

    public void setDiffusion(float diffusion) {
        this.diffusion = diffusion;
    }

    public float growth() {
        return growth;
    }

    public void setGrowth(float growth) {
        this.growth = growth;
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
