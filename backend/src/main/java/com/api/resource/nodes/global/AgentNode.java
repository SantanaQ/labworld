package com.api.resource.nodes.global;

import com.api.resource.nodes.EditorNode;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

import static com.api.resource.ObjectCaster.*;

public class AgentNode extends EditorNode {

    private int amount;
    private float hunger;
    private float heat;
    private float curiosity;
    private float fear;

    @JsonProperty("data")
    public void unpackData(Map<String, Object> data) {
        this.amount =  getInt(data, "amount");
        this.hunger = getFloat(data, "hunger");
        this.heat = getFloat(data, "heat");
        this.curiosity = getFloat(data, "curiosity");
        this.fear = getFloat(data, "fear");
    }

    public int amount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public float hunger() {
        return hunger;
    }

    public void setHunger(float hunger) {
        this.hunger = hunger;
    }

    public float heat() {
        return heat;
    }

    public void setHeat(float heat) {
        this.heat = heat;
    }

    public float curiosity() {
        return curiosity;
    }

    public void setCuriosity(float curiosity) {
        this.curiosity = curiosity;
    }

    public float fear() {
        return fear;
    }

    public void setFear(float fear) {
        this.fear = fear;
    }
}
