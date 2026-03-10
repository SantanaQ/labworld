package com.sim.signal;

public class HoleMaskNoise implements SignalSource {

    private final SignalSource base;
    private final SignalSource holes;

    private final float holeThreshold;
    private final float holeStrength;

    public HoleMaskNoise(
            SignalSource base,
            SignalSource holes,
            float holeThreshold,
            float holeStrength) {

        this.base = base;
        this.holes = holes;
        this.holeThreshold = holeThreshold;
        this.holeStrength = holeStrength;
    }

    @Override
    public float sample(float x, float y) {

        float v = base.sample(x, y);
        float h = holes.sample(x, y);

        if (h > holeThreshold) {
            v *= (1f - holeStrength);
        }

        return v;
    }
}