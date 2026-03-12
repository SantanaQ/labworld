package com.sim.layer.step;

import com.sim.layer.LayerContext;
import com.sim.layer.LayerID;

public interface LayerReferenceStep extends LayerStep {

    void resolve(LayerContext ctx);
    LayerID dependencyId();

}
