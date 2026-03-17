package com.sim.config;

import com.sim.layer.LayerID;

import java.util.UUID;

public class WorldConfig {

    public int width = 256;
    public int height = 256;
    private int seed = 42069;
    private int agentCount = 64;
    public UUID worldId;
    private int layerCount;

    public LayerConfig heatConfig;
    public LayerConfig foodConfig;
    public LayerConfig scentConfig;

    public WorldConfig() {
        setDefaults();
    }

    public WorldConfig(int width, int height, int seed, int agentCount) {
        this.width = width;
        this.height = height;
        this.seed = seed;
        this.agentCount = agentCount;
    }

    public void setHeatConfig(LayerConfig heatConfig) {
        this.heatConfig = heatConfig;
        this.layerCount++;
    }

    public void setFoodConfig(LayerConfig foodConfig) {
        this.foodConfig = foodConfig;
        this.layerCount++;
    }

    public void setScentConfig(LayerConfig scentConfig) {
        this.scentConfig = scentConfig;
        this.layerCount++;
    }

    public void setDefaults() {
        setHeatConfig(DefaultCfgs.defaultHeat(width, height, seed));
        setFoodConfig(DefaultCfgs.defaultFood(width, height, seed));
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

    public void setWorldId(UUID worldId) {
        this.worldId = worldId;
    }

    public LayerConfig configOf(LayerID id) {
        return switch (id) {
            case HEAT -> heatConfig;
            case FOOD -> foodConfig;
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
            case "food": setFoodConfig(stateLayer); break;
            case "scent": setScentConfig(stateLayer); break;
            default: throw new IllegalArgumentException("Potential layer " + id + " not supported");
        }
    }



}
