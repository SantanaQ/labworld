package com.sim.layers.time_behavior;

import com.sim.noise.SignalSource;

public class DomainWarp implements TimeBehavior {

    private final SignalSource warpX;
    private final SignalSource warpY;
    private final float strength;

    public DomainWarp(
            SignalSource warpX,
            SignalSource warpY,
            float strength) {
        this.warpX = warpX;
        this.warpY = warpY;
        this.strength = strength;
    }

    @Override
    public float sample(SignalSource src, float x, float y, float time) {

        float dx = warpX.sample(x + time, y + time) * strength;
        float dy = warpY.sample(x + time, y + time) * strength;

        return src.sample(x + dx, y + dy);
    }
}
