package world;

import com.sim.config.InteractiveLayerConfig;
import com.sim.config.LayerConfig;
import com.sim.config.ProceduralLayerConfig;
import com.sim.config.WorldConfig;
import com.sim.layers.LayerID;
import com.sim.layers.LayerRuntime;
import com.sim.layers.time_behavior.Fixed;
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

        WorldConfig config = new WorldConfig(width, height, seed, 0);
        LayerConfig foodCfg = new InteractiveLayerConfig(width, height, seed);
        foodCfg.setSignalSource(new GridSignal(food));
        foodCfg.setTimeBehavior(new Fixed());
        config.setFoodConfig(foodCfg);

        LayerConfig heatCfg = new ProceduralLayerConfig(width, height, seed);
        heatCfg.setSignalSource(new GridSignal(heat));
        heatCfg.setTimeBehavior(new Fixed());
        config.setHeatConfig(heatCfg);

        World world = new World(config);
        world.agents().clear();

        //world.layer(LayerID.FOOD).printValues();

        return world;
    }

    public static float[][] empty5x5() {
        return new float[][] {
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0}
        };
    }
}
