package com.sim.signal;

public class FractalNoise implements SignalSource {

    private final SignalSource base;
    private final int octaves;

    private final float[] amps;
    private final float[] freqs;
    private final float max;

    public FractalNoise(SignalSource base, int octaves, float persistence) {
        this.base = base;
        this.octaves = octaves;
        amps = new float[octaves];
        freqs = new float[octaves];

        float amp = 1f;
        float freq = 1f;
        float sum = 0f;

        for (int i = 0; i < octaves; i++) {
            amps[i] = amp;
            freqs[i] = freq;

            sum += amp;

            amp *= persistence;
            freq *= 2f;
        }

        max = sum;

    }

    public FractalNoise(int seed, int cellSize, int octaves, float persistence) {
        this(new ValueNoise(seed, cellSize), octaves, persistence);
    }

    @Override
    public float sample(float x, float y) {
        float value = 0f;

        for (int i = 0; i < octaves; i++) {
            value += base.sample(x * freqs[i], y * freqs[i]) * amps[i];
        }

        return value / max;
    }
}
