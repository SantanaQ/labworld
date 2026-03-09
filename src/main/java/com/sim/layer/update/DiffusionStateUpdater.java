package com.sim.layer.update;

import com.sim.layer.StateLayer;

public class DiffusionStateUpdater implements StateUpdater {

    private final float crossWeight = 0.20f;
    private final float diagWeight = 0.05f;

    private float diffusion; //0.2
    private float relaxation; //0.05
    private float decay; //0.955

    public DiffusionStateUpdater(float diffusion, float relaxation, float decay) {
        this.diffusion = diffusion;
        this.relaxation = relaxation;
        this.decay = decay;
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

                // diffusion
                s += diffusion * (kernel - s);

                // relaxation towards procedural potential
                s += relaxation * (p - s);

                // influence
                s += layer.influenceAt(x,y);
                s = Math.clamp(s, 0f, 1f);

                // decay
                s *= decay;

                layer.setNextState(x, y, s);
            }
        }
        layer.clearInfluence();
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
        float diag = (nw + ne + sw + se) * diagWeight;

        return cross + diag;
    }
}
