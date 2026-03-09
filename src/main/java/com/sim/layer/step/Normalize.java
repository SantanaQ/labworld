package com.sim.layer.step;

public final class Normalize implements LayerStep {

    private final float min;
    private final float max;

    public Normalize(float min, float max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public float apply(float value, int x, int y) {
        return (value - min) / (max - min);
    }
}
