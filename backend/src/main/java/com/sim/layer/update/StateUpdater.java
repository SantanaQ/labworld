package com.sim.layer.update;

import com.sim.layer.StateLayer;

public interface StateUpdater {

    void update(StateLayer layer, float[] potential, float[] state,
                float[] nextState, float[] influence);

    default float applyWeightedAvg(int width, int height,
                                   int x, int y,
                                   float[] state,
                                   float crossWeight, float diagWeight) {


        int y0 = Math.max(y-1, 0);
        int y1 = Math.min(y+1, height-1);
        int x0 = Math.max(x-1, 0);
        int x1 = Math.min(x+1, width-1);

        float n  = state[y0 * width + x];
        float s  = state[y1 * width + x];
        float w = state[y * width + x0];
        float e  = state[y * width + x1];

        float nw = state[y0 * width + x0];
        float ne = state[y0 * width + x1];
        float sw = state[y1 * width + x0];
        float se = state[y1 * width + x1];

        return (n+s+w+e)*crossWeight + (nw+ne+sw+se)*diagWeight;
    }


}

