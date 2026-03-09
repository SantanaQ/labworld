package world;

import com.sim.config.*;
import com.sim.layer.time_behavior.Fixed;
import com.sim.layer.update.CopyStateUpdater;
import com.sim.layer.update.DefaultPotentialUpdater;
import com.sim.layer.update.InactiveStateUpdater;
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

        StateLayerConfig foodCfg = new StateLayerConfig(width, height, seed);
        foodCfg.setSignalSource(new GridSignal(food));
        foodCfg.setTimeBehavior(new Fixed());
        foodCfg.setPotentialUpdater(new DefaultPotentialUpdater());
        foodCfg.setStateUpdater(new CopyStateUpdater());
        config.setFoodConfig(foodCfg);

        PotentialLayerConfig heatCfg = new PotentialLayerConfig(width, height, seed);
        heatCfg.setSignalSource(new GridSignal(heat));
        heatCfg.setTimeBehavior(new Fixed());
        heatCfg.setPotentialUpdater(new DefaultPotentialUpdater());
        config.setHeatConfig(heatCfg);

        PotentialLayerConfig scentCfg = new PotentialLayerConfig(width, height, seed);
        scentCfg.setSignalSource(new GridSignal(scent));
        scentCfg.setTimeBehavior(new Fixed());
        heatCfg.setPotentialUpdater(new DefaultPotentialUpdater());
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
