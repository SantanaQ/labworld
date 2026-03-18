package com.api.resource.nodes;


import com.api.resource.nodes.base_update.DefaultPotentialUpdaterNode;
import com.api.resource.nodes.global.AgentNode;
import com.api.resource.nodes.global.LayerNode;
import com.api.resource.nodes.global.WorldNode;
import com.api.resource.nodes.layerStep.*;
import com.api.resource.nodes.signal.ClusteredPatchNoiseNode;
import com.api.resource.nodes.signal.FractalNoiseNode;
import com.api.resource.nodes.signal.HoleMaskNoiseNode;
import com.api.resource.nodes.signal.ImageNode;
import com.api.resource.nodes.state_update.CopyStateUpdaterNode;
import com.api.resource.nodes.state_update.DiffusionAndGrowthStateUpdaterNode;
import com.api.resource.nodes.state_update.DiffusionAndRelaxationStateUpdaterNode;
import com.api.resource.nodes.time_behavior.CompositeNode;
import com.api.resource.nodes.time_behavior.DomainWarpNode;
import com.api.resource.nodes.time_behavior.DriftNode;
import com.api.resource.nodes.time_behavior.FixedNode;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Map;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DefaultPotentialUpdaterNode.class, name = "defaultPotentialUpdater"),
        @JsonSubTypes.Type(value = CopyStateUpdaterNode.class, name = "copyStateUpdater"),
        @JsonSubTypes.Type(value = FixedNode.class, name = "fixed"),
        @JsonSubTypes.Type(value = LayerNode.class, name = "heatLayer"),
        @JsonSubTypes.Type(value = LayerNode.class, name = "scentLayer"),
        @JsonSubTypes.Type(value = LayerNode.class, name = "supplyLayer"),

        @JsonSubTypes.Type(value = AgentNode.class, name = "agents"),
        @JsonSubTypes.Type(value = WorldNode.class, name = "world"),
        @JsonSubTypes.Type(value = BinaryThresholdNode.class, name = "binaryThreshold"),
        @JsonSubTypes.Type(value = SoftThresholdNode.class, name = "softThreshold"),
        @JsonSubTypes.Type(value = ClampNode.class, name = "clamp"),
        @JsonSubTypes.Type(value = NormalizeNode.class, name = "normalize"),
        @JsonSubTypes.Type(value = SuitabilityMaskNode.class, name = "suitabilityMask"),
        @JsonSubTypes.Type(value = SuitabilityDecayNode.class, name = "suitabilityDecay"),
        @JsonSubTypes.Type(value = ImageNode.class, name = "image"),
        @JsonSubTypes.Type(value = FractalNoiseNode.class, name = "fractalNoise"),
        @JsonSubTypes.Type(value = HoleMaskNoiseNode.class, name = "holeMaskNoise"),
        @JsonSubTypes.Type(value = ClusteredPatchNoiseNode.class, name = "clusteredPatchNoise"),
        @JsonSubTypes.Type(value = DiffusionAndGrowthStateUpdaterNode.class, name = "diffusionGrowthUpdater"),
        @JsonSubTypes.Type(value = DiffusionAndRelaxationStateUpdaterNode.class, name = "diffusionRelaxationUpdater"),
        @JsonSubTypes.Type(value = CompositeNode.class, name = "composite"),
        @JsonSubTypes.Type(value = DomainWarpNode.class, name = "domainWarp"),
        @JsonSubTypes.Type(value = DriftNode.class, name = "drift"),
})
public class EditorNode {

    private String id;
    private String category;
    private String type;

    public String id() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String category() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String type() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
