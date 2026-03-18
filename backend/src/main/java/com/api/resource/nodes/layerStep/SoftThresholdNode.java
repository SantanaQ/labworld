package com.api.resource.nodes.layerStep;

import com.api.resource.nodes.EditorNode;
import com.sim.layer.step.SoftThreshold;

public class SoftThresholdNode extends EditorNode<SoftThreshold> {

    private float threshold;
    private float softness;

    @Override
    public SoftThreshold build() {
        return new SoftThreshold(threshold, softness);
    }

    public float threshold() {
        return threshold;
    }

    public void setThreshold(float threshold) {
        this.threshold = threshold;
    }

    public float softness() {
        return softness;
    }

    public void setSoftness(float softness) {
        this.softness = softness;
    }
}
