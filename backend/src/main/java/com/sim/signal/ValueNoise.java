package com.sim.signal;

public class ValueNoise implements SignalSource {

    private final HashNoise hash;
    private final int cellSize;

    public ValueNoise(int seed, int cellSize) {
        this.hash = new HashNoise(seed);
        this.cellSize = cellSize;
    }

    @Override
    public float sample(float x, float y) {
        int xi = (int) Math.floor(x / cellSize);
        int yi = (int) Math.floor(y / cellSize);

        float fx = (x / cellSize) - xi;
        float fy = (y / cellSize) - yi;

        float v00 = hash.random(xi,     yi);
        float v10 = hash.random(xi + 1, yi);
        float v01 = hash.random(xi,     yi + 1);
        float v11 = hash.random(xi + 1, yi + 1);

        float ix0 = lerp(v00, v10, smooth(fx));
        float ix1 = lerp(v01, v11, smooth(fx));

        return lerp(ix0, ix1, smooth(fy));
    }

    private float lerp(float a, float b, float t) {
        return a + t * (b - a);
    }

    private float smooth(float t) {
        return t * t * (3 - 2 * t);
    }
}
