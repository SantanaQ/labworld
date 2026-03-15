package com.sim.signal;

import com.sim.utils.MathHelpers;

public class ValueNoise implements SignalSource {

    private final HashNoise hash;
    private final float invCellSize;

    public ValueNoise(int seed, int cellSize) {
        this.hash = new HashNoise(seed);
        this.invCellSize = 1f / cellSize;
    }

    @Override
    public float sample(float x, float y) {
        float gx = x * invCellSize;
        float gy = y * invCellSize;

        int xi = (int) gx;
        int yi = (int) gy;

        float fx = gx - xi;
        float fy = gy - yi;

        float v00 = hash.random(xi,     yi);
        float v10 = hash.random(xi + 1, yi);
        float v01 = hash.random(xi,     yi + 1);
        float v11 = hash.random(xi + 1, yi + 1);

        float sx = smooth(fx);
        float sy = smooth(fy);

        float ix0 = MathHelpers.lerp(v00, v10, sx);
        float ix1 = MathHelpers.lerp(v01, v11, sx);

        return MathHelpers.lerp(ix0, ix1, sy);
    }

    private float smooth(float t) {
        return t * t * (3 - 2 * t);
    }
}
