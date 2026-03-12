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

        // clustered habitat noise
        SignalSource foodSignal = new ClusteredPatchNoise(seed);

        c.setSignalSource(foodSignal);

        // drifting biome behaviour
        //c.setTimeBehavior(defaultDrift());

        // potential simply follows signal
        c.setPotentialUpdater(new DefaultPotentialUpdater());

        // food growth behaviour
        c.setStateUpdater(
                new DiffusionGrowthStateUpdater(
                        0.05f,   // diffusion (food drift)
                        0.12f,   // growthRate
                        0.998f,  // stateDecay
                        0.4f     // influenceDecay
                )
        );

        // create clear habitat zones
        c.addLayerStep(new SoftThreshold(
                0.55f,
                0.08f
        ));

        // heat suitability (food prefers certain temperatures)
        c.addLayerStep(new SuitabilityMask(
                LayerID.HEAT,
                0.15f,
                0.35f
        ));

        // decay if temperature becomes bad
        c.addLayerStep(new SuitabilityDecay(
                LayerID.HEAT,
                0.15f,
                0.65f,
                0.03f
        ));
        return c;
    }

    public static LayerConfig defaultScent(int width, int height, int seed) {
        StateLayerConfig c = new StateLayerConfig(width, height, seed);
        c.setSignalSource(new GridSignal(new float[height][width]));
        c.setTimeBehavior(new Fixed());
        c.setPotentialUpdater(new DefaultPotentialUpdater());
        c.setStateUpdater(new DiffusionRelaxationStateUpdater(0.5f, 0, 0.995f, 0.3f));
        return c;
    }

    public static LayerConfig defaultHeat(int width, int height, int seed) {
        PotentialLayerConfig c = new PotentialLayerConfig(width, height, seed);
        c.setSignalSource(new FractalNoise(
                seed+200,
                30,
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
                50);

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
