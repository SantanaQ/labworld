package com.api.resource.nodes.layerStep;

import com.api.resource.nodes.EditorNode;
import com.sim.layer.LayerID;
import com.sim.layer.step.SuitabilityMask;

public class SuitabilityMaskNode extends EditorNode<SuitabilityMask> {

    private String referenceLayer;
    private float min;
    private float max;

    @Override
    public SuitabilityMask build() {
        LayerID referenceId = LayerID.byString(referenceLayer);
        return new SuitabilityMask(referenceId, min, max);
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

    public String referenceLayer() {
        return referenceLayer;
    }

    public void setReferenceLayer(String referenceLayer) {
        this.referenceLayer = referenceLayer;
    }
}
