package com.sim.snapshot;

import com.sim.layer.WorldLayer;

public class LayerSnapshot {

    private final float[][] values = new float[3][3];

    public LayerSnapshot(WorldLayer original) {
        /*float[][] origVal = original.values();
        values = new float[origVal.length][origVal[0].length];
        for (int i = 0; i < origVal.length; i++) {
            System.arraycopy(origVal[i], 0, values[i], 0, origVal[0].length);
        }*/
    }

    public float[][] values() {
        return values;
    }

}
