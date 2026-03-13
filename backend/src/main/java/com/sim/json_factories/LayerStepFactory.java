package com.sim.json_factories;

import com.fasterxml.jackson.databind.JsonNode;
import com.sim.layer.LayerID;
import com.sim.layer.step.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class LayerStepFactory {

    private final Map<String, Function<JsonNode, LayerStep>> registry = new HashMap<>();

    public LayerStepFactory() {
        registry.put("BinaryThreshold", this::createBinaryThreshold);
        registry.put("SoftThreshold", this::createSoftThreshold);
        registry.put("SuitabilityMask", this::createSuitabilityMask);
        registry.put("SuitabilityDecay", this::createSuitabilityDecay);
        registry.put("Clamp", this::createClamp);
        registry.put("Normalize", this::createNormalize);
    }

    public LayerStep create(JsonNode node) {
        String type = node.get("type").asText();
        Function<JsonNode, LayerStep> creator = registry.get(type);
        if (creator == null) throw new IllegalArgumentException("Unknown step type: " + type);
        return creator.apply(node);
    }

    private LayerStep createSoftThreshold(JsonNode node) {
        float threshold = (float) node.get("threshold").asDouble();
        float softness = (float) node.get("softness").asDouble();
        return new SoftThreshold(threshold, softness);
    }

    private LayerStep createBinaryThreshold(JsonNode node) {
        float threshold = (float) node.get("threshold").asDouble();
        return new BinaryThreshold(threshold);
    }

    private LayerStep createSuitabilityMask(JsonNode node) {
        LayerID refId = LayerID.byString(node.get("refId").asText());
        float min = (float) node.get("min").asDouble();
        float max = (float) node.get("max").asDouble();
        return new SuitabilityMask(refId, min, max);
    }

    private LayerStep createSuitabilityDecay(JsonNode node) {
        LayerID refId = LayerID.byString(node.get("refId").asText());
        float min = (float) node.get("min").asDouble();
        float max = (float) node.get("max").asDouble();
        float decay = (float) node.get("decay").asDouble();
        return new SuitabilityDecay(refId, min, max, decay);
    }

    private LayerStep createClamp(JsonNode node) {
        float min = (float) node.get("min").asDouble();
        float max = (float) node.get("max").asDouble();
        return new Clamp(min, max);
    }

    private LayerStep createNormalize(JsonNode node) {
        float min = (float) node.get("min").asDouble();
        float max = (float) node.get("max").asDouble();
        return new Normalize(min, max);
    }


}
