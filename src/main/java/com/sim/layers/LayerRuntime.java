package com.sim.layers;

import com.sim.config.BaseLayerConfig;

public class LayerRuntime {

    private final LayerID id;
    private final BaseLayerConfig<?> cfg;
    private WorldLayer layer;

    public LayerRuntime(LayerID id, BaseLayerConfig<?> cfg) {
        this.id = id;
        this.cfg = cfg;
    }

    public void updateLayer(WorldLayer layer) {
        this.layer = layer;
    }

    public LayerID id() {
        return id;
    }

    public WorldLayer layer() {
        return layer;
    }

    public BaseLayerConfig<?> config() {
        return cfg;
    }


}
