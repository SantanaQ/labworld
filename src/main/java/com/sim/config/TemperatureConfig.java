package com.sim.config;

import com.sim.layers.LayerBuilder;
import com.sim.layers.LayerContext;
import com.sim.layers.ProceduralLayer;
import com.sim.layers.WorldLayer;
import com.sim.layers.step.Normalize;
import com.sim.layers.step.SoftThreshold;
import com.sim.layers.time_behavior.Composite;
import com.sim.layers.time_behavior.DomainWarp;
import com.sim.layers.time_behavior.Drifting;
import com.sim.layers.time_behavior.TimeBehavior;
import com.sim.noise.FractalNoise;
import com.sim.noise.ValueNoise;

import java.util.List;

public class TemperatureConfig extends BaseLayerConfig {

    private final int width;
    private final int height;
    private final int worldSeed;

    public boolean driftActive;
    public boolean warpActive;

    public float driftSpeed;
    public float driftAngle;

    public float threshold;
    public float softness;

    public int warpCellSize;
    public int warpOctaves;
    public float warpPersistence;
    public float warpStrength;

    public int signalCellSize;
    public int signalOctaves;
    public int signalPersistence;

    public TemperatureConfig(int width, int height, int worldSeed) {
        this.width = width;
        this.height = height;
        this.worldSeed = worldSeed;
    }

    private Drifting drift()
    {
        Drifting drift =  new Drifting(driftSpeed, driftAngle);
        drift.setActive(driftActive);
        return drift;
    }

    private DomainWarp warp()
    {
        DomainWarp warp = new DomainWarp(
                new FractalNoise(new ValueNoise(worldSeed+1, warpCellSize),
                        warpOctaves,
                        warpPersistence),
                new FractalNoise(new ValueNoise(worldSeed+2, warpCellSize),
                        warpOctaves,
                        warpPersistence),
                warpStrength);
        warp.setActive(warpActive);
        return warp;
    }

    @Override
    public ProceduralLayer buildLayer(LayerContext ctx)
    {
        TimeBehavior warpingDrift = new Composite(List.of(drift(), warp()));

        return new LayerBuilder(width, height)
                .withSignalSource(new FractalNoise(worldSeed+3,
                        signalCellSize,
                        signalOctaves,
                        signalPersistence))
                .withTimeBehaviour(warpingDrift)
                .step(new SoftThreshold(threshold, softness))
                .step(new Normalize(0, 1))
                .buildProceduralLayer();
    }

    public static TemperatureConfig defaultConfig(int width, int height, int seed) {
        TemperatureConfig c = new TemperatureConfig(width, height, seed);

        c.driftActive = true;
        c.warpActive = true;

        c.driftSpeed = 0.02f;
        c.driftAngle = 3.1415f;

        c.threshold = 0.2f;
        c.softness = 0.1f;

        c.warpCellSize = 64;
        c.warpOctaves = 3;
        c.warpPersistence = 0.6f;
        c.warpStrength = 4;

        c.signalCellSize = 50;
        c.signalOctaves = 2;
        c.signalPersistence = 5;
        return c;
    }



}
