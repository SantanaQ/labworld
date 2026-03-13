package com.sim.json_factories;

import com.fasterxml.jackson.databind.JsonNode;
import com.sim.layer.update.DefaultPotentialUpdater;
import com.sim.layer.update.PotentialUpdater;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PotentialUpdateFactory {

    private final Map<String, Function<JsonNode, PotentialUpdater>> registry = new HashMap<>();

    public PotentialUpdateFactory() {
        registry.put("Default", this::createDefault);
    }

    public PotentialUpdater create(JsonNode node) {
        String type = node.get("type").asText();
        Function<JsonNode, PotentialUpdater> creator = registry.get(type);
        if (creator == null) throw new IllegalArgumentException("Unknown update type: " + type);
        return creator.apply(node);
    }

    private PotentialUpdater createDefault(JsonNode node) {
        return new DefaultPotentialUpdater();
    }

}
