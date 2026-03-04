package com.sim.config;

import java.util.HashMap;
import java.util.Map;

public class WorldConfig {

    public int width = 200;
    public int height = 200;
    public int seed = 9992221;
    public int agentCount = 50;

    public TemperatureConfig temperatureConfig;
    public FoodConfig foodConfig;

    public WorldConfig() {
        this.temperatureConfig = TemperatureConfig.defaultConfig(width, height, seed);
        this.foodConfig = FoodConfig.defaultConfig(width, height, seed);
    }




}
