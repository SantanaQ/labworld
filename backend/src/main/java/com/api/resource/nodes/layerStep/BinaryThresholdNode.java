package com.api.resource.nodes.layerStep;

import com.api.resource.nodes.EditorNode;
import com.sim.layer.step.BinaryThreshold;

public class BinaryThresholdNode extends EditorNode<BinaryThreshold> {

    private float threshold;

    public float threshold() {
        return threshold;
    }

    public void setThreshold(float threshold) {
        this.threshold = threshold;
    }

    @Override
    public String category() {
        return "compositing";
    }

    @Override
    public BinaryThreshold build() {
        return new BinaryThreshold(threshold);
    }

}
