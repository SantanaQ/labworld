package com.sim.layers;

import com.sim.config.LayerConfig;

public class LayerRuntime {

    private final LayerID id;
    private final LayerConfig cfg;
    private WorldLayer layer;

    public LayerRuntime(LayerID id, LayerConfig cfg) {
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

    public LayerConfig config() {
        return cfg;
    }


}
