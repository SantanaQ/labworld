package com.sim.layer.update;

import com.sim.layer.StateLayer;

public interface StateUpdater {

    void update(float[][] potential, float[][] state,
                float[][] nextState, float[][] influence);

    default float applyWeightedAvg(int x, int y, float[][] state, float crossWeight, float diagWeight) {

        float n  = state[y-1][x];
        float s  = state[y+1][x];
        float w  = state[y][x-1];
        float e  = state[y][x+1];

        float nw = state[y-1][x-1];
        float ne = state[y-1][x+1];
        float sw = state[y+1][x-1];
        float se = state[y+1][x+1];

        float cross = (n + s + w + e) * crossWeight;
        float diag  = (nw + ne + sw + se) * diagWeight;

        return cross + diag;
    }


}

