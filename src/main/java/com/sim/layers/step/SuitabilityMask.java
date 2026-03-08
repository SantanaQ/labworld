package com.sim.layers.step;

import com.sim.layers.LayerContext;
import com.sim.layers.LayerID;
import com.sim.layers.WorldLayer;
import com.sim.world.Coordinate;
import com.sim.world.MathHelpers;

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
    public float apply(float value, Coordinate coordinate) {
        float ref = referenceLayer.accessibleAt(coordinate.x(), coordinate.y());
        float suitability = MathHelpers.smoothstep(min, max, ref);
        return value * suitability;
    }

    @Override
    public LayerID dependencyId() {
        return refId;
    }

}
