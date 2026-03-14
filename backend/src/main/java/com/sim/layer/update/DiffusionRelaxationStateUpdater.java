package com.sim.layer.update;

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
    public void update(float[][] potential, float[][] state,
                       float[][] nextState, float[][] influence) {
        int w = potential[0].length;
        int h = potential.length;

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {

                float s = state[y][x];
                float p = potential[y][x];

                float kernel = applyWeightedAvg(x, y, state, crossWeight, diagWeight);

                // diffusion
                s += diffusion * (kernel - s);

                // relaxation towards procedural potential
                s += relaxation * (p - s);

                // influence
                s += influence[y][x];
                s = Math.clamp(s, 0f, 1f);

                // decay
                s *= stateDecay;

                nextState[y][x] = s;

                influence[y][x] *= influenceDecay;
            }
        }
    }

}
