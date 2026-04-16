package com.sim.layer.update;

import com.sim.layer.StateLayer;

public class DiffusionRelaxationStateUpdater implements StateUpdater {

    private final float crossWeight = 0.20f;
    private final float diagWeight = 0.05f;

    private float diffusion;
    private float relaxation;
    private float stateDecay;
    private float influenceDecay;

    public DiffusionRelaxationStateUpdater(float diffusion, float relaxation,
                                           float stateDecay, float influenceDecay) {
        this.diffusion = diffusion;
        this.relaxation = relaxation;
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

                // diffusion
                s += diffusion * (kernel - s);

                // relaxation towards procedural potential
                s += relaxation * (p - s);

                // influence
                s += influence[idx] * (1f - s);
                s = Math.clamp(s, 0f, 1f);

                // decay
                s *= stateDecay;

                nextState[idx] = s;

                influence[idx] *= influenceDecay;
            }
        }
    }

}
