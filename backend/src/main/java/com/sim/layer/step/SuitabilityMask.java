package com.sim.layer.step;

import com.sim.layer.LayerContext;
import com.sim.layer.LayerID;
import com.sim.layer.WorldLayer;
import com.sim.utils.MathHelpers;

public class SuitabilityMask implements LayerReferenceStep {

    private final LayerID refId;
    private WorldLayer referenceLayer;

    private final float min;
    private final float max;
    private final float midRange;

    public SuitabilityMask(LayerID referenceLayer, float min, float max) {
        this.refId = referenceLayer;
        this.min = min;
        this.max = max;
        this.midRange = min + ((max - min) / 2);
    }

    @Override
    public void resolve(LayerContext ctx) {
        this.referenceLayer = ctx.get(refId);
    }

    @Override
    public float apply(float value, int x, int y) {
        float ref = referenceLayer.valueAt(x, y);

        /* this implements a bell curve where suitability rises towards
            midrange value nad shrinks towards max value
         */
        float fadeIn = MathHelpers.smoothstep(min, midRange, ref);
        float fadeOut = 1 -  MathHelpers.smoothstep(midRange, max, ref);

        float suitability = fadeIn * fadeOut;

        return value * suitability;
    }

    @Override
    public LayerID dependencyId() {
        return refId;
    }

}
