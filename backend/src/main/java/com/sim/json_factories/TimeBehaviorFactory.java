package com.sim.json_factories;

import com.api.resource.EditorGraphNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.sim.layer.time_behavior.*;
import com.sim.signal.FractalNoise;
import com.sim.signal.SignalField;
import com.sim.signal.SignalSource;
import com.sim.signal.ValueNoise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class TimeBehaviorFactory implements LayerFactory<TimeBehavior> {

    private final Map<String, Function<EditorGraphNode, TimeBehavior>> registry = new HashMap<>();

    public TimeBehaviorFactory() {
        registry.put("Fixed", this::createFixed);
        registry.put("Drift", this::createDrift);
        registry.put("DomainWarp", this::createDomainWarp);
        registry.put("Composite", this::createComposite);
    }

    public TimeBehavior create(EditorGraphNode node) {
        String type = node.nodeData().type();
        Function<EditorGraphNode, TimeBehavior> creator = registry.get(type);
        if (creator == null) throw new IllegalArgumentException("Unknown time type: " + type);
        return creator.apply(node);
    }

    private TimeBehavior createFixed(EditorGraphNode node) {
        return new Fixed();
    }

    private TimeBehavior createDrift(EditorGraphNode node) {
        float speed = (float) node.nodeData().get("speed");
        float angle = (float) node.nodeData().get("angle");
        return new Drift(speed, angle);
    }

    private TimeBehavior createDomainWarp(EditorGraphNode node) {
        int width = (int) node.nodeData().get("width");
        int height = (int) node.nodeData().get("height");

        int cellSizeX = (int) node.nodeData().get("cellSizeX");
        int octavesX = (int) node.nodeData().get("octavesX");
        float persistenceX = (float) node.nodeData().get("persistenceX");

        int cellSizeY = (int) node.nodeData().get("cellSizeY");
        int octavesY = (int) node.nodeData().get("octavesY");
        float persistenceY = (float) node.nodeData().get("persistenceY");

        float strength = (float) node.nodeData().get("strength");
        String seed = (String) node.nodeData().get("seed");
        int seedCode = seed.hashCode();

        SignalSource warpX = new FractalNoise(
                new ValueNoise(seedCode, cellSizeX), octavesX, persistenceX);
        SignalSource warpY = new FractalNoise(
                new ValueNoise(seedCode, cellSizeY), octavesY, persistenceY);
        SignalField fieldX = new SignalField(width,height, warpX);
        SignalField fieldY = new SignalField(width,height, warpY);

        return new DomainWarp(fieldX, fieldY, strength);
    }

    private TimeBehavior createComposite(EditorGraphNode node) {
        /*String[] children = node.nodeData().get("children");
        if (childrenNode == null || !childrenNode.isArray()) {
            throw new IllegalArgumentException("Composite must have a children array");
        }

        List<TimeBehavior> children = new ArrayList<>();
        for (JsonNode childNode : childrenNode) {
            children.add(create(childNode));
        }*/
        return new Composite(List.of());
    }

}
