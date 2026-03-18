package com.sim.json_factories;

import com.api.resource.EditorGraphNode;
import com.api.resource.nodes.EditorNode;
import com.api.resource.nodes.state_update.DiffusionAndGrowthStateUpdaterNode;
import com.api.resource.nodes.state_update.DiffusionAndRelaxationStateUpdaterNode;
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

    private static final Map<String, Function<EditorNode, StateUpdater>> registry = Map.of(
            "copyStateUpdater", StateUpdateFactory::createCopyStateUpdater,
            "diffusionGrowthUpdater", StateUpdateFactory::createDiffusionAndGrowthStateUpdater,
            "diffusionRelaxationUpdater", StateUpdateFactory::createDiffusionAndRelaxationStateUpdater
    );


    public static StateUpdater create(EditorNode node) {
        String type = node.type();
        Function<EditorNode, StateUpdater> creator = registry.get(type);
        if (creator == null) throw new IllegalArgumentException("Unknown update type: " + type);
        return creator.apply(node);
    }

    private static CopyStateUpdater createCopyStateUpdater(EditorNode node) {
        return new CopyStateUpdater();
    }

    private static DiffusionGrowthStateUpdater createDiffusionAndGrowthStateUpdater(EditorNode node) {
        DiffusionAndGrowthStateUpdaterNode dGUpdateNode = (DiffusionAndGrowthStateUpdaterNode) node;
        float diffusion = dGUpdateNode.diffusion();
        float growth = dGUpdateNode.growth();
        float stateDecay = dGUpdateNode.stateDecay();
        float influenceDecay = dGUpdateNode.influenceDecay();
        return new DiffusionGrowthStateUpdater(diffusion, growth, stateDecay, influenceDecay);
    }

    private static DiffusionRelaxationStateUpdater createDiffusionAndRelaxationStateUpdater(EditorNode node) {
        DiffusionAndRelaxationStateUpdaterNode dRUpdateNode = (DiffusionAndRelaxationStateUpdaterNode) node;
        float diffusion = dRUpdateNode.diffusion();
        float relaxation = dRUpdateNode.relaxation();
        float stateDecay = dRUpdateNode.stateDecay();
        float influenceDecay = dRUpdateNode.influenceDecay();
        return new DiffusionRelaxationStateUpdater(diffusion, relaxation, stateDecay, influenceDecay);
    }

}
