package com.sim.layers.step;

import com.sim.world.Coordinate;

public final class Clamp implements LayerStep{

    private final float min;
    private final float max;

    public Clamp(float min, float max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public float apply(float value, Coordinate coordinate) {
        return Math.clamp(value, min, max);
    }
}
