package com.sim.json_factories;

import com.api.resource.EditorGraphNode;
import com.api.resource.nodes.EditorNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.sim.layer.update.DefaultPotentialUpdater;
import com.sim.layer.update.PotentialUpdater;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PotentialUpdateFactory {

    private static final Map<String, Function<EditorNode, PotentialUpdater>> registry = Map.of(
            "defaultPotentialUpdater", PotentialUpdateFactory::createDefault
    );


    public static PotentialUpdater create(EditorNode node) {
        String type = node.type();
        Function<EditorNode, PotentialUpdater> creator = registry.get(type);
        if (creator == null) throw new IllegalArgumentException("Unknown update type: " + type);
        return creator.apply(node);
    }

    private static PotentialUpdater createDefault(EditorNode node) {
        return new DefaultPotentialUpdater();
    }

}
