package com.sim.layer.step;

import com.sim.layer.LayerContext;
import com.sim.layer.LayerID;
import com.sim.layer.WorldLayer;
import com.sim.world.Coordinate;
import com.sim.utils.MathHelpers;

public class SuitabilityMask implements LayerReferenceStep {

    private final LayerID refId;
    private WorldLayer referenceLayer;

    private final float min;
    private final float max;

    public SuitabilityMask(LayerID referenceLayer, float min, float max) {
        this.refId = referenceLayer;
        this.min = min;
        this.max = max;
    }

    @Override
    public void resolve(LayerContext ctx) {
        this.referenceLayer = ctx.get(refId);
    }

    @Override
    public float apply(float value, int x, int y) {
        float ref = referenceLayer.accessibleAt(x, y);
        float suitability = MathHelpers.smoothstep(min, max, ref);
        return value * suitability;
    }

    @Override
    public LayerID dependencyId() {
        return refId;
    }

}
