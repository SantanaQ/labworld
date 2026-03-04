package com.sim.layers.time_behavior;

import com.sim.noise.SignalSource;

public class DomainWarp implements TimeBehavior {

    private final SignalSource warpX;
    private final SignalSource warpY;
    private final float strength;

    private boolean active = true;

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
        if(!active) {
            return src.sample(x, y);
        }
        float dx = warpX.sample(x, y) * strength;
        float dy = warpY.sample(x, y) * strength;
        return src.sample(x + dx, y + dy);
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }
}
