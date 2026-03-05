package world;

import com.sim.config.FoodConfig;
import com.sim.config.HeatConfig;
import com.sim.config.WorldConfig;
import com.sim.signal.GridSignal;
import com.sim.world.World;
import com.sim.world.agent.Agent;

public class TestWorldBuilder {

    int width = 5;
    int height = 5;
    int seed = 1;

    float[][] heat;
    float[][] food;

    Agent agent;

    public TestWorldBuilder heat(float[][] grid) {
        this.heat = grid;
        return this;
    }

    public TestWorldBuilder agent(Agent agent) {
        this.agent = agent;
        return this;
    }

    public TestWorldBuilder food(float[][] grid) {
        this.food = grid;
        return this;
    }

    public World build() {

        HeatConfig heatConfig = HeatConfig.defaultConfig(width, height, seed);
        heatConfig.driftActive = false;
        heatConfig.warpActive = false;
        heatConfig.signalSource = new GridSignal(heat);

        FoodConfig foodConfig = FoodConfig.defaultConfig(width, height, seed);
        foodConfig.driftActive = false;
        foodConfig.signalSource = new GridSignal(food);

        WorldConfig config = new WorldConfig(width, height, seed, 0);
        config.setHeatConfig(heatConfig);
        config.setFoodConfig(foodConfig);

        World world = new World(config);
        world.agents().clear();
        world.spawnAgent(agent);

        return world;
    }
}
