package com.sim.snapshot;

import com.sim.layer.Renderable;

public class LayerSnapshot {

    private final float[][] values;

    public LayerSnapshot(Renderable original) {
        float[][] origVal = original.renderValues();
        values = new float[origVal.length][origVal[0].length];
        for (int i = 0; i < origVal.length; i++) {
            System.arraycopy(origVal[i], 0, values[i], 0, origVal[0].length);
        }
    }

    public float[][] values() {
        return values;
    }

}
