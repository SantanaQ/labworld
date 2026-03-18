package com.sim.json_factories;

import com.api.resource.EditorGraphNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.sim.layer.update.CopyStateUpdater;
import com.sim.layer.update.DiffusionGrowthStateUpdater;
import com.sim.layer.update.DiffusionRelaxationStateUpdater;
import com.sim.layer.update.StateUpdater;
import com.sim.signal.SignalSource;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class StateUpdateFactory implements LayerFactory<StateUpdater>{

    private final Map<String, Function<EditorGraphNode, StateUpdater>> registry = new HashMap<>();

    public StateUpdateFactory() {
        registry.put("Copy", this::createCopyStateUpdater);
        registry.put("DiffusionAndGrowth", this::createDiffusionAndGrowthStateUpdater);
        registry.put("DiffusionAndRelaxation", this::createDiffusionAndRelaxationStateUpdater);
    }

    public StateUpdater create(EditorGraphNode node) {
        String type = node.nodeData().type();
        Function<EditorGraphNode, StateUpdater> creator = registry.get(type);
        if (creator == null) throw new IllegalArgumentException("Unknown update type: " + type);
        return creator.apply(node);
    }

    private StateUpdater createCopyStateUpdater(EditorGraphNode node) {
        return new CopyStateUpdater();
    }

    private StateUpdater createDiffusionAndGrowthStateUpdater(EditorGraphNode node) {
        float diffusion = (float) node.nodeData().get("diffusion");
        float growthRate = (float) node.nodeData().get("growth");
        float stateDecay = (float) node.nodeData().get("stateDecay");
        float influenceDecay = (float) node.nodeData().get("influenceDecay");
        return new DiffusionGrowthStateUpdater(diffusion, growthRate, stateDecay, influenceDecay);
    }

    private StateUpdater createDiffusionAndRelaxationStateUpdater(EditorGraphNode node) {
        float diffusion = (float) node.nodeData().get("diffusion");
        float relaxation = (float) node.nodeData().get("relaxation");
        float stateDecay = (float) node.nodeData().get("stateDecay");
        float influenceDecay = (float) node.nodeData().get("influenceDecay");
        return new DiffusionRelaxationStateUpdater(diffusion, relaxation, stateDecay, influenceDecay);
    }

}
