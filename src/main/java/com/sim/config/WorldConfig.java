package com.sim.config;

public class WorldConfig {

    public int width = 200;
    public int height = 200;
    public int seed = 9992221;
    public int agentCount = 50;

    public HeatConfig heatConfig;
    public FoodConfig foodConfig;

    public WorldConfig() {
        this.heatConfig = HeatConfig.defaultConfig(width, height, seed);
        this.foodConfig = FoodConfig.defaultConfig(width, height, seed);
    }

    public WorldConfig(int width, int height, int seed, int agentCount) {
        this.width = width;
        this.height = height;
        this.seed = seed;
        this.agentCount = agentCount;
    }

    public void setHeatConfig(HeatConfig heatConfig) {
        this.heatConfig = heatConfig;
    }

    public void setFoodConfig(FoodConfig foodConfig) {
        this.foodConfig = foodConfig;
    }

}
