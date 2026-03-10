package com.sim.layer.step;

import com.sim.layer.LayerContext;
import com.sim.layer.LayerID;
import com.sim.layer.WorldLayer;
import com.sim.utils.MathHelpers;

public class SuitabilityDecay implements LayerReferenceStep {

    private final LayerID refId;
    private WorldLayer referenceLayer;

    private final float min;
    private final float max;
    private final float decay;

    public SuitabilityDecay(LayerID referenceLayer, float min, float max, float decay) {
        this.refId = referenceLayer;
        this.min = min;
        this.max = max;
        this.decay = decay;
    }

    @Override
    public void resolve(LayerContext ctx) {
        this.referenceLayer = ctx.get(refId);
    }

    @Override
    public float apply(float value, int x, int y) {

        float ref = referenceLayer.valueAt(x, y);

        float suitability = MathHelpers.smoothstep(min, max, ref);

        float decayFactor = 1f - suitability;

        value *= suitability + decayFactor * decay;

        return value;
    }

    @Override
    public LayerID dependencyId() {
        return refId;
    }
}
