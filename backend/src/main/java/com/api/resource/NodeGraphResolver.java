package com.api.resource;

import com.api.resource.nodes.EditorNode;
import com.api.resource.nodes.global.AgentNode;
import com.api.resource.nodes.global.WorldNode;
import com.sim.config.LayerConfig;
import com.sim.config.WorldConfig;
import com.sim.layer.LayerID;
import java.util.List;

public class NodeGraphResolver {

    private final EditorGraph editorGraph;

    public NodeGraphResolver(EditorGraph editorGraph) {
        this.editorGraph = editorGraph;
    }

    public WorldConfig configFromGraph() {
        WorldNode worldNode = (WorldNode) editorGraph.getNode("world").nodeData();
        AgentNode agentNode = (AgentNode) editorGraph.getNode("agents").nodeData();

        WorldConfig worldConfig = LayerAssembler.buildWorldConfig(worldNode, agentNode);
        int width = worldNode.width();
        int height = worldNode.height();

        LayerConfig heatConfig = layerFromGraph(LayerID.HEAT, width, height);
        worldConfig.setHeatConfig(heatConfig);

        LayerConfig supplyConfig = layerFromGraph(LayerID.SUPPLY, width, height);
        worldConfig.setSupplyConfig(supplyConfig);

        LayerConfig scentConfig = layerFromGraph(LayerID.SCENT, width, height);
        worldConfig.setScentConfig(scentConfig);

        return worldConfig;
    }

    private LayerConfig layerFromGraph(LayerID id, int width, int height) {
        switch (id) {
            case HEAT -> {
                List<EditorNode> order = editorGraph.executionOrderOfLayer(LayerID.HEAT);
                return LayerAssembler.buildPotentialLayerConfig(order, width, height);
            }
            case SUPPLY -> {
                List<EditorNode> order = editorGraph.executionOrderOfLayer(LayerID.SUPPLY);
                return LayerAssembler.buildStateLayerConfig(order, width, height);
            }
            case SCENT -> {
                List<EditorNode> order = editorGraph.executionOrderOfLayer(LayerID.SCENT);
                return LayerAssembler.buildStateLayerConfig(order, width, height);
            }
            default -> {
                throw new RuntimeException("Unknown layer " + id);
            }
        }
    }

}
