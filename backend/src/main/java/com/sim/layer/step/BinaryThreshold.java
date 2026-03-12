package com.sim.layer.step;

public class BinaryThreshold implements LayerStep{

    private final float threshold;

    public BinaryThreshold(float threshold) {
        this.threshold = threshold;
    }

    @Override
    public float apply(float value, int x, int y) {
        if (value > threshold) {
            return 1;
        }

        return 0f;
    }

}
