package com.sim.config;

import com.sim.layers.LayerBuilder;
import com.sim.layers.ProceduralLayer;
import com.sim.layers.step.Normalize;
import com.sim.layers.step.SoftThreshold;
import com.sim.layers.time_behavior.Composite;
import com.sim.layers.time_behavior.DomainWarp;
import com.sim.layers.time_behavior.Drifting;
import com.sim.layers.time_behavior.TimeBehavior;
import com.sim.noise.FractalNoise;
import com.sim.noise.ValueNoise;

import java.util.List;

public class TemperatureConfig {

    private final int width;
    private final int height;
    private final int worldSeed;

    public boolean driftActive = true;
    public boolean warpActive = true;

    public float driftSpeed = 0.02f;
    public float driftAngle = 3.1415f;

    public float threshold = 0.2f;
    public float softness = 0.1f;

    public int warpCellSize = 64;
    public int warpOctaves = 3;
    public float warpPersistence = 0.6f;
    public float warpStrength = 4;

    public int signalCellSize = 50;
    public int signalOctaves = 2;
    public int signalPersistence = 5;

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

    public ProceduralLayer buildLayer()
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




}
