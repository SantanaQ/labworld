package com.sim.json_factories;

import com.api.resource.EditorGraphNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.sim.layer.LayerID;
import com.sim.layer.step.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class LayerStepFactory implements LayerFactory<LayerStep>{

    private final Map<String, Function<EditorGraphNode, LayerStep>> registry = new HashMap<>();

    public LayerStepFactory() {
        registry.put("binaryThreshold", this::createBinaryThreshold);
        registry.put("softThreshold", this::createSoftThreshold);
        registry.put("suitabilityMask", this::createSuitabilityMask);
        registry.put("suitabilityDecay", this::createSuitabilityDecay);
        registry.put("clamp", this::createClamp);
        registry.put("normalize", this::createNormalize);
    }

    @Override
    public LayerStep create(EditorGraphNode node) {
        String type = node.nodeData().type();
        Function<EditorGraphNode, LayerStep> creator = registry.get(type);
        if (creator == null) throw new IllegalArgumentException("Unknown step type: " + type);
        return creator.apply(node);
    }

    private LayerStep createSoftThreshold(EditorGraphNode node) {
        float threshold = (float) node.nodeData().get("threshold");
        float softness = (float) node.nodeData().get("softness");
        return new SoftThreshold(threshold, softness);
    }

    private LayerStep createBinaryThreshold(EditorGraphNode node) {
        float threshold = (float) node.nodeData().get("threshold");
        return new BinaryThreshold(threshold);
    }

    private LayerStep createSuitabilityMask(EditorGraphNode node) {
        LayerID refId = node.referenceLayer().orElseThrow();
        float min = (float) node.nodeData().get("min");
        float max = (float) node.nodeData().get("max");
        return new SuitabilityMask(refId, min, max);
    }

    private LayerStep createSuitabilityDecay(EditorGraphNode node) {
        LayerID refId = node.referenceLayer().orElseThrow();
        float min = (float) node.nodeData().get("min");
        float max = (float) node.nodeData().get("max");
        float decay = (float) node.nodeData().get("decay");
        return new SuitabilityDecay(refId, min, max, decay);
    }

    private LayerStep createClamp(EditorGraphNode node) {
        float min = (float) node.nodeData().get("min");
        float max = (float) node.nodeData().get("max");
        return new Clamp(min, max);
    }

    private LayerStep createNormalize(EditorGraphNode node) {
        float min = (float) node.nodeData().get("min");
        float max = (float) node.nodeData().get("max");
        return new Normalize(min, max);
    }


}
