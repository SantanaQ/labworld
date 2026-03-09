package com.sim.config;

import com.sim.layer.LayerID;
import com.sim.layer.step.SoftThreshold;
import com.sim.layer.step.SuitabilityMask;
import com.sim.layer.time_behavior.Composite;
import com.sim.layer.time_behavior.DomainWarp;
import com.sim.layer.time_behavior.Drifting;
import com.sim.layer.time_behavior.Fixed;
import com.sim.layer.update.DefaultPotentialUpdater;
import com.sim.layer.update.DiffusionStateUpdater;
import com.sim.signal.FractalNoise;
import com.sim.signal.GridSignal;
import com.sim.signal.ValueNoise;

import java.util.List;

public final class DefaultCfgs {

    private DefaultCfgs() {}

    public static LayerConfig defaultFood(int width, int height, int seed) {
        StateLayerConfig c = new StateLayerConfig(width, height, seed);
        c.setSignalSource(new FractalNoise(
                c.seed+100,
                70,
                2,
                5));
        c.setTimeBehavior(defaultDrift());
        c.setPotentialUpdater(new DefaultPotentialUpdater());
        c.setStateUpdater(new DiffusionStateUpdater(0.2f, 0.05f, 0.955f));
        c.addLayerStep(new SoftThreshold(0.7f, 0.1f));
        c.addLayerStep(new SuitabilityMask(
                LayerID.HEAT,
                0.5f,
                0.7f));
        return c;
    }

    public static LayerConfig defaultScent(int width, int height, int seed) {
        StateLayerConfig c = new StateLayerConfig(width, height, seed);
        c.setSignalSource(new GridSignal(new float[height][width]));
        c.setTimeBehavior(new Fixed());
        c.setPotentialUpdater(new DefaultPotentialUpdater());
        c.setStateUpdater(new DiffusionStateUpdater(0.2f, 0.05f, 0.955f));
        return c;
    }

    public static LayerConfig defaultHeat(int width, int height, int seed) {
        PotentialLayerConfig c = new PotentialLayerConfig(width, height, seed);
        c.setSignalSource(new FractalNoise(
                seed+200,
                100,
                2,
                5));

        DomainWarp warp = new DomainWarp(
                new FractalNoise(
                        new ValueNoise(seed+1, 64),
                        3,
                        0.6f),
                new FractalNoise(
                        new ValueNoise(seed+2, 64),
                        3,
                        0.6f),
                20);

        Composite warpingDrift = new Composite(List.of(defaultDrift(), warp));
        c.setTimeBehavior(warpingDrift);
        c.addLayerStep(new SoftThreshold(0.2f, 0.1f));
        c.setPotentialUpdater(new DefaultPotentialUpdater());
        return c;
    }

    private static Drifting defaultDrift() {
        return new Drifting(0.05f, 0);
    }


}
