package com.sim.layer.update;

import com.sim.layer.StateLayer;

public class DirectInfluenceStateUpdater implements StateUpdater{
    @Override
    public void update(StateLayer layer) {
        int w = layer.width();
        int h = layer.height();

        for(int y = 0; y < h; y++){
            for(int x = 0; x < w; x++){
                float s = layer.stateAt(x, y) + layer.influenceAt(x, y);
                s = Math.clamp(s, 0, 1);
                layer.setNextState(x,y, s);
            }
        }
        layer.swapState();
        layer.decayInfluence(0);

    }
}
