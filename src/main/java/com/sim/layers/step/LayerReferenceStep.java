package com.sim.layers.step;

import com.sim.layers.LayerContext;
import com.sim.layers.LayerID;

public interface LayerReferenceStep extends LayerStep {

    void resolve(LayerContext ctx);
    LayerID dependencyId();

}
