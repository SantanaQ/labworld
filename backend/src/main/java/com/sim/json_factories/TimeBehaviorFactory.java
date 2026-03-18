package com.sim.json_factories;

import com.api.resource.EditorGraphNode;
import com.api.resource.nodes.EditorNode;
import com.api.resource.nodes.time_behavior.DomainWarpNode;
import com.api.resource.nodes.time_behavior.DriftNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.sim.layer.time_behavior.*;
import com.sim.signal.FractalNoise;
import com.sim.signal.SignalField;
import com.sim.signal.SignalSource;
import com.sim.signal.ValueNoise;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class TimeBehaviorFactory {

    private static final Map<String, Function<EditorNode, TimeBehavior>> registry = Map.of(
            "Fixed", TimeBehaviorFactory::createFixed,
            "Drift", TimeBehaviorFactory::createDrift,
            "DomainWarp", TimeBehaviorFactory::createDomainWarp,
            "Composite", TimeBehaviorFactory::createComposite
    );



    public static TimeBehavior create(EditorNode node) {
        String type = node.type();
        Function<EditorNode, TimeBehavior> creator = registry.get(type);
        if (creator == null) throw new IllegalArgumentException("Unknown time type: " + type);
        return creator.apply(node);
    }

    private static Fixed createFixed(EditorNode node) {
        return new Fixed();
    }

    private static Drift createDrift(EditorNode node) {
        DriftNode driftNode = (DriftNode) node;
        float speed = driftNode.speed();
        float angle = driftNode.angle();
        return new Drift(speed, angle);
    }

    private static DomainWarp createDomainWarp(EditorNode node) {
        DomainWarpNode domainWarpNode = (DomainWarpNode) node;
        int width = domainWarpNode.width();
        int height = domainWarpNode.height();

        int cellSizeX = domainWarpNode.cellSizeX();
        int octavesX = domainWarpNode.octavesX();
        float persistenceX = domainWarpNode.persistenceX();

        int cellSizeY = domainWarpNode.cellSizeY();
        int octavesY = domainWarpNode.octavesY();
        float persistenceY = domainWarpNode.persistenceY();

        float strength = domainWarpNode.strength();
        String seed = domainWarpNode.seed();
        int seedCode = seed.hashCode();

        SignalSource warpX = new FractalNoise(
                new ValueNoise(seedCode, cellSizeX), octavesX, persistenceX);
        SignalSource warpY = new FractalNoise(
                new ValueNoise(seedCode, cellSizeY), octavesY, persistenceY);
        SignalField fieldX = new SignalField(width,height, warpX);
        SignalField fieldY = new SignalField(width,height, warpY);

        return new DomainWarp(fieldX, fieldY, strength);
    }

    private static Composite createComposite(EditorNode node) {
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
