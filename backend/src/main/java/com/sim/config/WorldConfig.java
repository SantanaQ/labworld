package com.sim.config;

import com.sim.agent.Needs;
import com.sim.layer.LayerID;

import java.util.UUID;

public class WorldConfig {

    public int width = 256;
    public int height = 256;
    private int seed = 42069;

    private int agentCount = 64;
    private Needs agentNeeds = new Needs();

    public UUID worldId;
    private int layerCount;

    public LayerConfig heatConfig;
    public LayerConfig supplyConfig;
    public LayerConfig scentConfig;

    public WorldConfig() {
        setDefaults();
    }

    public WorldConfig(int width, int height, String seed, int agentCount) {
        this.width = width;
        this.height = height;
        this.seed = seed.hashCode();
        this.agentCount = agentCount;
    }

    public void setHeatConfig(LayerConfig heatConfig) {
        this.heatConfig = heatConfig;
        this.layerCount++;
    }

    public void setSupplyConfig(LayerConfig supplyConfig) {
        this.supplyConfig = supplyConfig;
        this.layerCount++;
    }

    public void setScentConfig(LayerConfig scentConfig) {
        this.scentConfig = scentConfig;
        this.layerCount++;
    }

    public void setDefaults() {
        setHeatConfig(DefaultCfgs.defaultHeat(width, height, seed));
        setSupplyConfig(DefaultCfgs.defaultFood(width, height, seed));
        setScentConfig(DefaultCfgs.defaultScent(width, height, seed));
    }

    public UUID worldId() {
        return worldId;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public int seed() {
        return seed;
    }

    public int layerCount() {
        return layerCount;
    }

    public int agentCount() {
        return agentCount;
    }

    public void setAgentNeeds(Needs agentNeeds) {
        this.agentNeeds = agentNeeds;
    }

    public Needs agentNeeds() {
        return agentNeeds;
    }

    public void setWorldId(UUID worldId) {
        this.worldId = worldId;
    }



    public LayerConfig configOf(LayerID id) {
        return switch (id) {
            case HEAT -> heatConfig;
            case SUPPLY -> supplyConfig;
            case SCENT -> scentConfig;
        };
    }

    public void addPotentialLayer(String id, PotentialLayerConfig potentialLayer) {
        if (id.equals("heat")) {
            setHeatConfig(potentialLayer);
        } else {
            throw new IllegalArgumentException("Potential layer " + id + " not supported");
        }
    }

    public void addStateLayer(String id, StateLayerConfig stateLayer) {
        switch (id) {
            case "food": setSupplyConfig(stateLayer); break;
            case "scent": setScentConfig(stateLayer); break;
            default: throw new IllegalArgumentException("Potential layer " + id + " not supported");
        }
    }



}
