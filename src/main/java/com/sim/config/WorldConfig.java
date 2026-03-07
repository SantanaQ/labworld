package com.sim.config;

public class WorldConfig {

    public int width = 200;
    public int height = 200;
    public int seed = 9992221;
    public int agentCount = 50;

    public LayerConfig heatConfig;
    public LayerConfig foodConfig;
    public LayerConfig scentConfig;

    public WorldConfig() {
        this.heatConfig = DefaultCfgs.defaultHeat(width, height, seed);
        this.foodConfig = DefaultCfgs.defaultFood(width, height, seed);
        this.scentConfig = DefaultCfgs.defaultScent(width, height, seed);
    }

    public WorldConfig(int width, int height, int seed, int agentCount) {
        this.width = width;
        this.height = height;
        this.seed = seed;
        this.agentCount = agentCount;
    }

    public void setHeatConfig(LayerConfig heatConfig) {
        this.heatConfig = heatConfig;
    }

    public void setFoodConfig(LayerConfig foodConfig) {
        this.foodConfig = foodConfig;
    }

    public void setScentConfig(LayerConfig scentConfig) {
        this.scentConfig = scentConfig;
    }

}
