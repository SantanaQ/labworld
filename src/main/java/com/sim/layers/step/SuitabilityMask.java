package com.sim.layers.step;

import com.sim.layers.WorldLayer;
import com.sim.world.Coordinate;

public class SuitabilityMask implements LayerStep{

    private final WorldLayer referenceLayer;
    private final float min;
    private final float max;

    public SuitabilityMask(WorldLayer referenceLayer, float min, float max) {
        this.referenceLayer = referenceLayer;
        this.min = min;
        this.max = max;
    }

    @Override
    public float apply(float value, Coordinate coordinate) {
        float ref = referenceLayer.accessibleAt(coordinate);
        float suitability = smoothstep(min, max, ref);
        return value * suitability;
    }
}
