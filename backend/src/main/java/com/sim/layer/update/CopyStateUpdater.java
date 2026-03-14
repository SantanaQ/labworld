package com.sim.layer.update;

public class CopyStateUpdater implements StateUpdater{

    @Override
    public void update(float[][] potential, float[][] state,
                       float[][] nextState, float[][] influence) {
        int w = potential[0].length;
        int h = potential.length;

        float s;
        for(int y = 0; y < h; y++){
            for(int x = 0; x < w; x++){
                s = potential[y][x] + influence[y][x];
                nextState[y][x] = s;
                influence[y][x] *= 0.5f;
            }
        }
    }
}
