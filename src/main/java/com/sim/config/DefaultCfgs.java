package com.sim.config;

import com.sim.layers.LayerID;
import com.sim.layers.step.SoftThreshold;
import com.sim.layers.step.SuitabilityMask;
import com.sim.layers.time_behavior.Composite;
import com.sim.layers.time_behavior.DomainWarp;
import com.sim.layers.time_behavior.Drifting;
import com.sim.signal.FractalNoise;
import com.sim.signal.ValueNoise;

import java.util.List;

public final class DefaultCfgs {

    private DefaultCfgs() {}

    public static LayerConfig defaultFood(int width, int height, int seed) {
        LayerConfig c = new InteractiveLayerConfig(width, height, seed);
        c.setSignalSource(new FractalNoise(
                c.seed+100,
                70,
                2,
                5));
        c.setTimeBehavior(defaultDrift());
        c.addLayerStep(new SoftThreshold(0.7f, 0.1f));
        c.addLayerStep(new SuitabilityMask(
                LayerID.HEAT,
                0.5f,
                0.7f));
        return c;
    }

    public static LayerConfig defaultHeat(int width, int height, int seed) {
        LayerConfig c = new ProceduralLayerConfig(width, height, seed);
        c.setSignalSource(new FractalNoise(
                seed+200,
                50,
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
        return c;
    }

    private static Drifting defaultDrift() {
        return new Drifting(0.1f, 0);
    }


}
