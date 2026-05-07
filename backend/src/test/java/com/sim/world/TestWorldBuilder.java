package com.sim.world;

import com.sim.config.*;
import com.sim.layer.time_behavior.Fixed;
import com.sim.layer.update.CopyStateUpdater;
import com.sim.layer.update.DefaultPotentialUpdater;
import com.sim.layer.update.DiffusionRelaxationStateUpdater;
import com.sim.signal.GridSignal;

public class TestWorldBuilder {

    int size;
    String seed = "seed";

    float[][] heat;
    float[][] supply;
    float[][] scent;

    public TestWorldBuilder(int size) {
        this.size = size;
        this.heat = valueGrid(0, size);
        this.supply = valueGrid(0, size);
        this.scent = valueGrid(0, size);
    }

    public TestWorldBuilder withHeat(float[][] grid) {
        this.heat = grid;
        return this;
    }

    public TestWorldBuilder withSupply(float[][] grid) {
        this.supply = grid;
        return this;
    }

    public TestWorldBuilder withScent(float[][] grid) {
        this.scent = grid;
        return this;
    }


    public World build() {

        WorldConfig config = new WorldConfig(size, size, seed, 0);

        StateLayerConfig supplyCfg = new StateLayerConfig(size, size);
        supplyCfg.setSignalSource(new GridSignal(supply));
        supplyCfg.setTimeBehavior(new Fixed());
        supplyCfg.setPotentialUpdater(new DefaultPotentialUpdater());
        supplyCfg.setStateUpdater(new CopyStateUpdater());
        config.setSupplyConfig(supplyCfg);

        PotentialLayerConfig heatCfg = new PotentialLayerConfig(size, size);
        heatCfg.setSignalSource(new GridSignal(heat));
        heatCfg.setTimeBehavior(new Fixed());
        heatCfg.setPotentialUpdater(new DefaultPotentialUpdater());
        config.setHeatConfig(heatCfg);

        StateLayerConfig scentCfg = new StateLayerConfig(size, size);
        scentCfg.setSignalSource(new GridSignal(scent));
        scentCfg.setTimeBehavior(new Fixed());
        scentCfg.setPotentialUpdater(new DefaultPotentialUpdater());
        supplyCfg.setStateUpdater(new CopyStateUpdater());
        config.setScentConfig(scentCfg);

        return new World(config);
    }


    public static float[][] valueGrid(float value, int size) {
        float[][] grid = new float[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = value;
            }
        }
        return grid;
    }

    public static float[][] gradientGrid(char direction, float from, float to, int size) {
        if (from >= to) {
            throw new IllegalArgumentException(
                    "from value " + from + " is greater than to value " + to);
        }

        if (size <= 1) {
            throw new IllegalArgumentException("size must be greater than 1");
        }

        float[][] grid = new float[size][size];
        float step = (to - from) / (size - 1);

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {

                int pos = switch (direction) {
                    case 'N' -> y;
                    case 'S' -> size - 1 - y;
                    case 'E' -> size - 1 - x;
                    case 'W' -> x;
                    default -> throw new IllegalArgumentException(
                            "Invalid direction: " + direction);
                };

                grid[y][x] = to - step * pos;
            }
        }

        return grid;
    }


}
