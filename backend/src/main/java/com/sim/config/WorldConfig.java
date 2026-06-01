package com.sim.config;

import com.sim.agent.Needs;
import java.util.UUID;

public class WorldConfig {

    public final int width;
    public final int height;
    private final int seed;

    private final int agentCount;
    private Needs agentNeeds = new Needs();

    public UUID worldId;

    public LayerConfig heatConfig;
    public LayerConfig supplyConfig;
    public LayerConfig scentConfig;
    public LayerConfig trailConfig;
    public LayerConfig stressConfig;

    private int layerCount = 0;

    public WorldConfig(int width, int height, String seed, int agentCount) {
        this.width = width;
        this.height = height;
        this.seed = seed.hashCode();
        this.agentCount = agentCount;
    }

    public void setHeatConfig(LayerConfig heatConfig) {
        this.heatConfig = heatConfig;
        layerCount++;
    }

    public void setSupplyConfig(LayerConfig supplyConfig) {
        this.supplyConfig = supplyConfig;
        layerCount++;
    }

    public void setScentConfig(LayerConfig scentConfig) {
        this.scentConfig = scentConfig;
        layerCount++;
    }

    public void setTrailConfig(LayerConfig trailConfig) {
        this.trailConfig = trailConfig;
        layerCount++;
    }

    public void setStressConfig(LayerConfig stressConfig) {
        this.stressConfig = stressConfig;
        layerCount++;
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

    public int layerCount() {
        return layerCount;
    }

}
