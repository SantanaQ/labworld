package com.sim.layer.update;

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
    public void update(float[][] potential, float[][] state,
                       float[][] nextState, float[][] influence) {

        int w = potential[0].length;
        int h = potential.length;

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {

                float s = state[y][x];
                float p = potential[y][x];

                float kernel = applyWeightedAvg(x, y, state, crossWeight, diagWeight);

                s += diffusion * (kernel - s);

                // slow growth only if potential allows it
                if (p > s) {
                    s += growthRate * p * (1 - s);
                }

                s += influence[y][x];

                s *= stateDecay;

                s = Math.clamp(s, 0f, 1f);

                nextState[y][x] = s;

                influence[y][x] *= influenceDecay;
            }
        }
    }


}
