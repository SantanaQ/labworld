package com.sim.json_factories;

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

public class TimeBehaviorFactory {

    private final Map<String, Function<JsonNode, TimeBehavior>> registry = new HashMap<>();

    public TimeBehaviorFactory() {
        registry.put("Fixed", this::createFixed);
        registry.put("Drift", this::createDrift);
        registry.put("DomainWarp", this::createDomainWarp);
        registry.put("Composite", this::createComposite);
    }

    public TimeBehavior create(JsonNode node) {
        String type = node.get("type").asText();
        Function<JsonNode, TimeBehavior> creator = registry.get(type);
        if (creator == null) throw new IllegalArgumentException("Unknown time type: " + type);
        return creator.apply(node);
    }

    private TimeBehavior createFixed(JsonNode node) {
        return new Fixed();
    }

    private TimeBehavior createDrift(JsonNode node) {
        float speed = (float) node.get("speed").asDouble();
        float angle = (float) node.get("angle").asDouble();
        return new Drift(speed, angle);
    }

    private TimeBehavior createDomainWarp(JsonNode node) {
        int width = node.get("width").asInt();
        int height = node.get("height").asInt();

        int cellSizeX = node.get("cellSizeX").asInt();
        int octavesX = node.get("octavesX").asInt();
        float persistenceX = (float) node.get("persistenceX").asDouble();

        int cellSizeY = node.get("cellSizeY").asInt();
        int octavesY = node.get("octavesY").asInt();
        float persistenceY = (float) node.get("persistenceY").asDouble();

        float strength = (float) node.get("strength").asDouble();
        int seed = node.get("seed").asInt();

        SignalSource warpX = new FractalNoise(
                new ValueNoise(seed, cellSizeX), octavesX, persistenceX);
        SignalSource warpY = new FractalNoise(
                new ValueNoise(seed, cellSizeY), octavesY, persistenceY);
        SignalField fieldX = new SignalField(width,height, warpX);
        SignalField fieldY = new SignalField(width,height, warpY);

        return new DomainWarp(fieldX, fieldY, strength);
    }

    private TimeBehavior createComposite(JsonNode node) {
        JsonNode childrenNode = node.get("children");
        if (childrenNode == null || !childrenNode.isArray()) {
            throw new IllegalArgumentException("Composite must have a children array");
        }

        List<TimeBehavior> children = new ArrayList<>();
        for (JsonNode childNode : childrenNode) {
            children.add(create(childNode));
        }
        return new Composite(children);
    }

}
