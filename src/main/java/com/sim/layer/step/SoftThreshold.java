package com.sim.layer.step;


public final class SoftThreshold implements LayerStep{

    private final float t0;
    private final float t1;

    public SoftThreshold(float threshold, float softness) {
        this.t0 = threshold - softness;
        this.t1 = threshold + softness;
    }


    @Override
    public float apply(float value, int x, int y) {
        if (value <= t0) return 0f;
        if (value >= t1) return value;

        float k = (value - t0) / (t1 - t0);

        return value * k;
    }

}
