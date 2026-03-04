package com.sim.config;

import com.sim.layers.InteractiveLayer;
import com.sim.layers.LayerBuilder;
import com.sim.layers.LayerContext;
import com.sim.layers.LayerID;
import com.sim.layers.step.Normalize;
import com.sim.layers.step.SoftThreshold;
import com.sim.layers.step.SuitabilityMask;
import com.sim.layers.time_behavior.Drifting;
import com.sim.noise.FractalNoise;

public class FoodConfig extends BaseLayerConfig<InteractiveLayer>{

    private final int width;
    private final int height;
    private final int worldSeed;

    public boolean driftActive;

    public float driftSpeed;
    public float driftAngle;

    public float threshold;
    public float softness;

    public LayerID suitabilityRef;
    public float suitabilityMin;
    public float suitabilityMax;

    public int signalCellSize;
    public int signalOctaves;
    public int signalPersistence;

    public FoodConfig(int width, int height, int seed) {
        this.width = width;
        this.height = height;
        this.worldSeed = seed;
    }

    private Drifting drift() {
        Drifting drift = new Drifting(driftSpeed, driftAngle);
        drift.setActive(driftActive);
        return drift;
    }

    private SuitabilityMask suitabilityMask(LayerContext ctx) {
        return new SuitabilityMask(
                ctx.get(suitabilityRef),
                suitabilityMin,
                suitabilityMax);
    }

    @Override
    public InteractiveLayer buildLayer(LayerContext ctx) {
        return new LayerBuilder(width, height)
                .withSignalSource(new FractalNoise(
                        worldSeed+100,
                        signalCellSize,
                        signalOctaves,
                        signalPersistence))
                .withTimeBehaviour(drift())
                .step(new SoftThreshold(threshold, softness))
                .step(suitabilityMask(ctx))
                .step(new Normalize(0, 1))
                .buildInteractiveLayer();
    }

    public static FoodConfig defaultConfig(int width, int height, int seed) {
        FoodConfig c = new FoodConfig(width, height, seed);
        c.driftActive = true;
        c.driftSpeed = 0.02f;
        c.driftAngle = 3.1415f;

        c.signalCellSize = 70;
        c.signalOctaves = 2;
        c.signalPersistence = 5;

        c.threshold = 0.7f;
        c.softness = 0.1f;

        c.suitabilityRef = LayerID.TEMPERATURE;
        c.suitabilityMin = 0.5f;
        c.suitabilityMax = 0.7f;

        return c;
    }


}
