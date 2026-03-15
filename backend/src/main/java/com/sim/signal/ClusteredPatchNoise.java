package com.sim.signal;

import com.sim.utils.MathHelpers;

public class ClusteredPatchNoise implements SignalSource {

    private final SignalSource base;
    private final SignalSource detail;

    private final float threshold;
    private final float softness;
    private final float holeStrength;

    public ClusteredPatchNoise(int seed) {

        this.base = new FractalNoise(seed, 18, 3, 0.5f);
        this.detail = new FractalNoise(seed + 100, 8, 2, 0.6f);

        this.threshold = 0.55f;
        this.softness = 0.08f;
        this.holeStrength = 0.35f;
    }

    public ClusteredPatchNoise(SignalSource base, SignalSource detail,
                               float threshold, float softness, float holeStrength) {
        this.base = base;
        this.detail = detail;
        this.threshold = threshold;
        this.softness = softness;
        this.holeStrength = holeStrength;
    }

    @Override
    public float sample(float x, float y) {

        float b = base.sample(x, y);

        // strengthen patch centers
        b = b * b;

        // smooth threshold -> patches
        float patch = MathHelpers.smoothstep(threshold - softness, threshold + softness, b);

        // holes inside patches
        float d = detail.sample(x, y);
        float holes = d * holeStrength;

        float result = patch - holes;

        return Math.clamp(result, 0f, 1f);
    }

}