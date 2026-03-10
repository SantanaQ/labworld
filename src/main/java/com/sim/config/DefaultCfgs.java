package com.sim.config;

import com.sim.layer.LayerID;
import com.sim.layer.step.*;
import com.sim.layer.time_behavior.Composite;
import com.sim.layer.time_behavior.DomainWarp;
import com.sim.layer.time_behavior.Drifting;
import com.sim.layer.time_behavior.Fixed;
import com.sim.layer.update.*;
import com.sim.signal.*;

import java.util.List;

public final class DefaultCfgs {

    private DefaultCfgs() {}

    public static LayerConfig defaultFood(int width, int height, int seed) {
        StateLayerConfig c = new StateLayerConfig(width, height, seed);

        SignalSource base = new FractalNoise(seed, 2, 3, 0.5f);
        SignalSource holes = new FractalNoise(seed+200, 25, 2, 0.6f);

        SignalSource foodSignal = new HoleMaskNoise(
                base,
                holes,
                0.65f,
                1f);

        c.setSignalSource(foodSignal);
        c.setTimeBehavior(defaultDrift());
        c.setPotentialUpdater(new DefaultPotentialUpdater());
        c.setStateUpdater(new CopyStateUpdater());
        c.addLayerStep(new SoftThreshold(0.7f, 0.1f));
        c.addLayerStep(new SuitabilityMask(
                LayerID.HEAT,
                0.4f,
                0.7f));
        c.addLayerStep(new SuitabilityDecay(
                LayerID.HEAT,
                0.6f,
                0.7f,
                0.95f));
        return c;
    }

    public static LayerConfig defaultScent(int width, int height, int seed) {
        StateLayerConfig c = new StateLayerConfig(width, height, seed);
        c.setSignalSource(new GridSignal(new float[height][width]));
        c.setTimeBehavior(new Fixed());
        c.setPotentialUpdater(new DefaultPotentialUpdater());
        //c.setStateUpdater(new DiffusionStateUpdater(0.2f, 0.05f, 0.955f));
        c.setStateUpdater(new DiffusionStateUpdater(1.0f, 0.15f, 0.998f, 0.96f));
        //c.setStateUpdater(new DirectInfluenceStateUpdater());
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
