package com.sim.layer;

import com.sim.config.LayerConfig;
import com.sim.layer.step.LayerStep;

import java.util.List;

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

    public List<LayerStep> layerSteps()
    {
        return cfg.layerSteps();
    }

}
