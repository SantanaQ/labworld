package world;

import com.sim.config.InteractiveLayerConfig;
import com.sim.config.LayerConfig;
import com.sim.config.ProceduralLayerConfig;
import com.sim.config.WorldConfig;
import com.sim.layer.time_behavior.Fixed;
import com.sim.signal.GridSignal;
import com.sim.world.World;

public class TestWorldBuilder {

    int width = 3;
    int height = 3;
    int seed = 1;

    float[][] heat = empty3x3();
    float[][] food = empty3x3();
    float[][] scent = empty3x3();

    public TestWorldBuilder heat(float[][] grid) {
        this.heat = grid;
        return this;
    }

    public TestWorldBuilder food(float[][] grid) {
        this.food = grid;
        return this;
    }

    public TestWorldBuilder scent(float[][] grid) {
        this.scent = grid;
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

        LayerConfig scentCfg = new InteractiveLayerConfig(width, height, seed);
        scentCfg.setSignalSource(new GridSignal(scent));
        scentCfg.setTimeBehavior(new Fixed());
        config.setScentConfig(scentCfg);

        return new World(config);
    }

    private static float[][] empty3x3() {
        return new float[][] {
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
        };
    }
}
