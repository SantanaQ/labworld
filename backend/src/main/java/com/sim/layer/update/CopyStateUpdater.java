package com.sim.layer.update;

import com.sim.layer.StateLayer;

public class CopyStateUpdater implements StateUpdater{

    @Override
    public void update(StateLayer layer, float[] potential, float[] state,
                       float[] nextState, float[] influence) {
        int w = layer.width;
        int h = layer.height;

        int idx;
        float s;
        for(int y = 0; y < h; y++){
            for(int x = 0; x < w; x++){
                idx = y * w + x;
                s = potential[idx] + influence[idx];
                nextState[idx] = s;
                influence[idx] *= 0.5f;
            }
        }
    }
}
