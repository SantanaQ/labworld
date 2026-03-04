package com.sim.config;

import java.util.HashMap;
import java.util.Map;

public class WorldConfig {

    public int width = 200;
    public int height = 200;
    public int seed = 9992221;
    public int agentCount = 50;

    public TemperatureConfig temperatureConfig;

    public WorldConfig() {
        this.temperatureConfig = new TemperatureConfig(width, height, seed);
    }

}
