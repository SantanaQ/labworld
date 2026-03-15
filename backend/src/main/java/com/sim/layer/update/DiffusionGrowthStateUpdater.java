package com.sim.layer.update;

import com.sim.layer.StateLayer;

public class DiffusionGrowthStateUpdater implements StateUpdater {

    private final float crossWeight = 0.20f;
    private final float diagWeight  = 0.05f;

    private float diffusion;
    private float growthRate;
    private float stateDecay;
    private float influenceDecay;

    public DiffusionGrowthStateUpdater(
            float diffusion,
            float growthRate,
            float stateDecay,
            float influenceDecay) {

        this.diffusion = diffusion;
        this.growthRate = growthRate;
        this.stateDecay = stateDecay;
        this.influenceDecay = influenceDecay;
    }

    @Override
    public void update(StateLayer layer, float[] potential, float[] state,
                       float[] nextState, float[] influence) {

        int w = layer.width;
        int h = layer.height;

        int idx;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                idx = y * w + x;

                float s = state[idx];
                float p = potential[idx];

                float kernel = applyWeightedAvg(w, h, x, y, state, crossWeight, diagWeight);

                s += diffusion * (kernel - s);

                // slow growth only if potential allows it
                if (p > s) {
                    s += growthRate * p * (1 - s);
                }

                s += influence[idx];

                s *= stateDecay;

                s = Math.clamp(s, 0f, 1f);

                nextState[idx] = s;

                influence[idx] *= influenceDecay;
            }
        }
    }


}
