package com.sim.layer.update;

import com.sim.layer.PotentialLayer;

public class DefaultPotentialUpdater implements PotentialUpdater {

    @Override
    public void update(PotentialLayer layer, float[] potential, float time) {
        int h = layer.height;
        int w = layer.width;

        float val;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                val = layer.applyTime(x, y, time);
                val = layer.applyCompositing(val, x, y);
                potential[y * w + x] = val;
            }
        }
    }
}
