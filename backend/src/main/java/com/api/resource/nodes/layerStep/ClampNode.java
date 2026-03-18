package com.api.resource.nodes.layerStep;

import com.api.resource.nodes.EditorNode;
import com.sim.layer.step.Clamp;

public class ClampNode extends EditorNode<Clamp> {

    private float min;
    private float max;

    @Override
    public Clamp build() {
        return new Clamp(min, max);
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
