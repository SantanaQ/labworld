package com.sim.json_factories;

import com.api.resource.nodes.EditorNode;
import com.api.resource.nodes.layerStep.*;
import com.sim.layer.LayerID;
import com.sim.layer.step.*;

import java.util.Map;
import java.util.function.Function;

public class LayerStepFactory {

    private static final Map<String, Function<EditorNode, LayerStep>> registry = Map.of(
            "binaryThreshold", LayerStepFactory::createBinaryThreshold,
            "softThreshold", LayerStepFactory::createSoftThreshold,
            "suitabilityMask", LayerStepFactory::createSuitabilityMask,
            "suitabilityDecay", LayerStepFactory::createSuitabilityDecay,
            "clamp", LayerStepFactory::createClamp,
            "normalize", LayerStepFactory::createNormalize
    );


    public static LayerStep create(EditorNode node) {
        String type = node.type();
        Function<EditorNode, LayerStep> creator = registry.get(type);
        if (creator == null) throw new IllegalArgumentException("Unknown step type: " + type);
        return creator.apply(node);
    }

    private static SoftThreshold createSoftThreshold(EditorNode node) {
        SoftThresholdNode softThresholdNode = (SoftThresholdNode) node;
        float threshold = softThresholdNode.threshold();
        float softness = softThresholdNode.softness();
        return new SoftThreshold(threshold, softness);
    }

    private static BinaryThreshold createBinaryThreshold(EditorNode node) {
        BinaryThresholdNode binaryThresholdNode = (BinaryThresholdNode) node;
        float threshold = binaryThresholdNode.threshold();
        return new BinaryThreshold(threshold);
    }

    private static SuitabilityMask createSuitabilityMask(EditorNode node) {
        SuitabilityMaskNode suitabilityMaskNode = (SuitabilityMaskNode) node;
        String reference = suitabilityMaskNode.reference();
        LayerID refId = LayerID.byString(reference);
        float min = suitabilityMaskNode.min();
        float max = suitabilityMaskNode.max();
        return new SuitabilityMask(refId, min, max);
    }

    private static SuitabilityDecay createSuitabilityDecay(EditorNode node) {
        SuitabilityDecayNode suitabilityDecayNode = (SuitabilityDecayNode) node;
        String reference = suitabilityDecayNode.reference();
        LayerID refId = LayerID.byString(reference);
        float min = suitabilityDecayNode.min();
        float max = suitabilityDecayNode.max();
        float decay = suitabilityDecayNode.decay();
        return new SuitabilityDecay(refId, min, max, decay);
    }

    private static Clamp createClamp(EditorNode node) {
        ClampNode clampNode = (ClampNode) node;
        float min = clampNode.min();
        float max = clampNode.max();
        return new Clamp(min, max);
    }

    private static Normalize createNormalize(EditorNode node) {
        NormalizeNode normalizeNode = (NormalizeNode) node;
        float min = normalizeNode.min();
        float max = normalizeNode.max();
        return new Normalize(min, max);
    }


}
