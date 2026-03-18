package com.api.resource.nodes.layerStep;

import com.api.resource.nodes.EditorNode;
import com.sim.layer.step.Normalize;

public class NormalizeNode extends EditorNode<Normalize> {

    private float min;
    private float max;

    @Override
    public Normalize build() {
        return new Normalize(min, max);
    }

    public float min() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public float max() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }
}
