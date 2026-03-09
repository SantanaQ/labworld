package com.sim.layer;

import java.util.HashMap;
import java.util.Map;

public class LayerContext {

    private final Map<LayerID, WorldLayer> registry = new HashMap<>();

    public void register(LayerID id, WorldLayer layer) {
        registry.put(id, layer);
    }

    public WorldLayer get(LayerID id) {
        return registry.get(id);
    }

    public void clear() {
        registry.clear();
    }

}
