package com.sim.layer.update;

import com.sim.layer.StateLayer;

public class CopyStateUpdater implements StateUpdater{

    @Override
    public void update(StateLayer layer) {
        int w = layer.width();
        int h = layer.height();

        for(int y = 0; y < h; y++){
            for(int x = 0; x < w; x++){
                layer.setNextState(x,y, layer.potentialAt(x, y));
            }
        }
        layer.swapState();

    }
}
