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
    public void update(StateLayer layer) {

        int w = layer.width();
        int h = layer.height();

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {

                float s = layer.stateAt(x, y);
                float p = layer.potentialAt(x, y);

                float kernel = applyWeightedAvg(x, y, layer);

                s += diffusion * (kernel - s);


                // slow growth only if potential allows it
                if (p > s) {
                    s += growthRate * p * (1 - s);
                }

                s += layer.influenceAt(x, y);

                s *= stateDecay;

                s = Math.clamp(s, 0f, 1f);

                layer.setNextState(x, y, s);
            }
        }

        layer.decayInfluence(influenceDecay);
        layer.swapState();
    }

    private float applyWeightedAvg(int x, int y, StateLayer layer) {

        float n  = layer.valueAt(x, y-1);
        float s  = layer.valueAt(x, y+1);
        float w  = layer.valueAt(x-1, y);
        float e  = layer.valueAt(x+1, y);

        float nw = layer.valueAt(x-1, y-1);
        float ne = layer.valueAt(x+1, y-1);
        float sw = layer.valueAt(x-1, y+1);
        float se = layer.valueAt(x+1, y+1);

        float cross = (n + s + w + e) * crossWeight;
        float diag  = (nw + ne + sw + se) * diagWeight;

        return cross + diag;
    }
}
