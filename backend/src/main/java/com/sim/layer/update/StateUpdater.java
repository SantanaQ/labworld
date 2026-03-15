package com.sim.layer.update;

import com.sim.layer.StateLayer;

public interface StateUpdater {

    void update(float[][] potential, float[][] state,
                float[][] nextState, float[][] influence);

    default float applyWeightedAvg(int x, int y, float[][] state, float crossWeight, float diagWeight) {
        int h = state.length;
        int w = state[0].length;

        int y0 = Math.max(y-1, 0);
        int y1 = Math.min(y+1, h-1);
        int x0 = Math.max(x-1, 0);
        int x1 = Math.min(x+1, w-1);

        float n  = state[y0][x];
        float s  = state[y1][x];
        float w_ = state[y][x0];
        float e  = state[y][x1];

        float nw = state[y0][x0];
        float ne = state[y0][x1];
        float sw = state[y1][x0];
        float se = state[y1][x1];

        return (n+s+w_+e)*crossWeight + (nw+ne+sw+se)*diagWeight;
    }


}

