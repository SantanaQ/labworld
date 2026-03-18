package com.sim.json_factories;

import com.api.resource.EditorGraphNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.sim.layer.update.DefaultPotentialUpdater;
import com.sim.layer.update.PotentialUpdater;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PotentialUpdateFactory implements LayerFactory<PotentialUpdater>{

    private final Map<String, Function<EditorGraphNode, PotentialUpdater>> registry = new HashMap<>();

    public PotentialUpdateFactory() {
        registry.put("defaultPotentialUpdater", this::createDefault);
    }

    @Override
    public PotentialUpdater create(EditorGraphNode node) {
        String type = node.nodeData().type();
        Function<EditorGraphNode, PotentialUpdater> creator = registry.get(type);
        if (creator == null) throw new IllegalArgumentException("Unknown update type: " + type);
        return creator.apply(node);
    }

    private PotentialUpdater createDefault(EditorGraphNode node) {
        return new DefaultPotentialUpdater();
    }

}
