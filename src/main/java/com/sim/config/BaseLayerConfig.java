package com.sim.config;

import com.sim.layers.LayerContext;
import com.sim.layers.WorldLayer;

public abstract class BaseLayerConfig<T extends WorldLayer> {
    private boolean dirty = true;

    public void markDirty() { dirty = true; }
    public boolean isDirty() { return dirty; }
    public void clearDirty() { dirty = false; }

    public abstract T buildLayer(LayerContext ctx);
}
