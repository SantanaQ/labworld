package com.sim.json_factories;

import com.fasterxml.jackson.databind.JsonNode;
import com.sim.layer.update.CopyStateUpdater;
import com.sim.layer.update.DiffusionGrowthStateUpdater;
import com.sim.layer.update.DiffusionRelaxationStateUpdater;
import com.sim.layer.update.StateUpdater;
import com.sim.signal.SignalSource;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class StateUpdateFactory {

    private final Map<String, Function<JsonNode, StateUpdater>> registry = new HashMap<>();

    public StateUpdateFactory() {
        registry.put("Copy", this::createCopyStateUpdater);
        registry.put("DiffusionAndGrowth", this::createDiffusionAndGrowthStateUpdater);
        registry.put("DiffusionAndRelaxation", this::createDiffusionAndRelaxationStateUpdater);
    }

    public StateUpdater create(JsonNode node) {
        String type = node.get("type").asText();
        Function<JsonNode, StateUpdater> creator = registry.get(type);
        if (creator == null) throw new IllegalArgumentException("Unknown update type: " + type);
        return creator.apply(node);
    }

    private StateUpdater createCopyStateUpdater(JsonNode node) {
        return new CopyStateUpdater();
    }

    private StateUpdater createDiffusionAndGrowthStateUpdater(JsonNode node) {
        float diffusion = node.get("diffusion").floatValue();
        float growthRate = node.get("growthRate").floatValue();
        float stateDecay = node.get("stateDecay").floatValue();
        float influenceDecay = node.get("influenceDecay").floatValue();
        return new DiffusionGrowthStateUpdater(diffusion, growthRate, stateDecay, influenceDecay);
    }

    private StateUpdater createDiffusionAndRelaxationStateUpdater(JsonNode node) {
        float diffusion = node.get("diffusion").floatValue();
        float relaxation = node.get("relaxation").floatValue();
        float stateDecay = node.get("stateDecay").floatValue();
        float influenceDecay = node.get("influence").floatValue();
        return new DiffusionRelaxationStateUpdater(diffusion, relaxation, stateDecay, influenceDecay);
    }

}
