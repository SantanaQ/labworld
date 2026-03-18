package com.api.resource;

import com.sim.agent.Needs;
import com.sim.config.LayerConfig;
import com.sim.config.PotentialLayerConfig;
import com.sim.config.StateLayerConfig;
import com.sim.config.WorldConfig;
import com.sim.json_factories.FactoryRegistry;
import com.sim.layer.LayerID;
import com.sim.layer.step.LayerStep;
import com.sim.layer.update.PotentialUpdater;
import com.sim.layer.update.StateUpdater;
import com.sim.signal.SignalSource;

import java.util.List;
import java.util.UUID;

public class NodeGraphResolver {

    private final EditorGraph editorGraph;

    public NodeGraphResolver(EditorGraph editorGraph) {
        this.editorGraph = editorGraph;
    }

    public WorldConfig configFromGraph() {
        EditorGraphNode worldNode = editorGraph.root();
        EditorGraphNode agentNode = editorGraph.getNode("agents");

        int width = (int) worldNode.nodeData().data().get("width");
        int height = (int) worldNode.nodeData().data().get("height");
        String seed = (String) worldNode.nodeData().data().get("seed");

        int agentCount = (int) agentNode.nodeData().data().get("amount");
        float hunger = ((Double) agentNode.nodeData().data().get("hunger")).floatValue();
        float heat = (float) agentNode.nodeData().data().get("heat");
        float curiosity = (float) agentNode.nodeData().data().get("curiosity");
        float fear = (float) agentNode.nodeData().data().get("fear");
        Needs agentNeeds = new Needs(hunger, curiosity, fear, 1, heat);

        WorldConfig config = new WorldConfig(width, height, seed, agentCount);
        config.setAgentNeeds(agentNeeds);
        config.setWorldId(UUID.randomUUID());

        LayerConfig heatConfig = layerFromGraph(LayerID.HEAT, width, height);
        config.setHeatConfig(heatConfig);

        LayerConfig supplyConfig = layerFromGraph(LayerID.FOOD, width, height);
        config.setFoodConfig(supplyConfig);

        LayerConfig scentConfig = layerFromGraph(LayerID.SCENT, width, height);
        config.setScentConfig(scentConfig);

        return config;
    }

    private LayerConfig layerFromGraph(LayerID id, int width, int height) {
        switch (id) {
            case HEAT -> {
                LayerConfig heatConfig = new PotentialLayerConfig(width, height);
                List<EditorGraphNode> order = editorGraph.executionOrderOfLayer(LayerID.HEAT);
                resolveOrder(heatConfig, order);
                return heatConfig;
            }
            case FOOD -> {
                LayerConfig foodConfig = new StateLayerConfig(width, height);
                List<EditorGraphNode> order = editorGraph.executionOrderOfLayer(LayerID.FOOD);
                resolveOrder(foodConfig, order);
                return foodConfig;
            }
            case SCENT -> {
                LayerConfig scentConfig = new PotentialLayerConfig(width, height);
                List<EditorGraphNode> order = editorGraph.executionOrderOfLayer(LayerID.SCENT);
                resolveOrder(scentConfig, order);
                return scentConfig;
            }
            default -> {
                throw new RuntimeException("Unknown layer " + id);
            }
        }

    }

    private void resolveOrder(LayerConfig layerCfg, List<EditorGraphNode> order) {
        FactoryRegistry registry = new FactoryRegistry();
        for (EditorGraphNode node : order) {
            switch (node.nodeData().category()) {
                case "Compositing" -> {
                    LayerStep step = (LayerStep) registry.get("Compositing").create(node);
                    layerCfg.addLayerStep(step);
                }
                case "Signal" -> {
                    SignalSource signal = (SignalSource) registry.get("Signal").create(node);
                    layerCfg.setSignalSource(signal);
                }
                case "Base Update" -> {
                    PotentialUpdater update = (PotentialUpdater) registry.get("Base Update").create(node);
                    ((PotentialLayerConfig) layerCfg).setPotentialUpdater(update);
                }
                case "State Update" -> {
                    StateUpdater update = (StateUpdater) registry.get("State Update").create(node);
                    ((StateLayerConfig) layerCfg).setStateUpdater(update);
                }
            }
        }
    }



}
