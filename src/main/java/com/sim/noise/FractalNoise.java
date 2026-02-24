package com.sim.noise;

public class FractalNoise implements SignalSource {

    private final SignalSource base;
    private final int octaves;
    private final float persistence;

    public FractalNoise(SignalSource base, int octaves, float persistence) {
        this.base = base;
        this.octaves = octaves;
        this.persistence = persistence;
    }

    @Override
    public float sample(float x, float y) {
        float value = 0f;
        float amp = 1f;
        float freq = 1f;
        float max = 0f;

        for (int i = 0; i < octaves; i++) {
            value += base.sample(x * freq, y * freq) * amp;
            max += amp;
            amp *= persistence;
            freq *= 2f;
        }
        return value / max;
    }
}
