package com.sim.snapshot;

public class LayerDelta {

    private int size;
    private int[] indexes;
    private float[] values;

    public int size() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int[] indexes() {
        return indexes;
    }

    public void setIndexes(int[] indexes) {
        this.indexes = indexes;
    }

    public float[] values() {
        return values;
    }

    public void setValues(float[] values) {
        this.values = values;
    }
}
