package com.api.resource;

import com.api.resource.nodes.EditorNode;
import com.api.resource.nodes.global.AgentNode;
import com.api.resource.nodes.global.WorldNode;
import com.sim.agent.Needs;
import com.sim.config.PotentialLayerConfig;
import com.sim.config.StateLayerConfig;
import com.sim.config.WorldConfig;
import com.sim.json_factories.*;
import com.sim.layer.step.LayerStep;
import com.sim.layer.time_behavior.TimeBehavior;
import com.sim.layer.update.PotentialUpdater;
import com.sim.layer.update.StateUpdater;
import com.sim.signal.SignalSource;

import java.util.List;
import java.util.UUID;

public class LayerAssembler {

    public static WorldConfig buildWorldConfig(WorldNode worldNode, AgentNode agentNode) {
        int width = worldNode.width();
        int height = worldNode.height();
        String seed = worldNode.seed();

        int agentAmount = agentNode.amount();
        float hunger = agentNode.hunger();
        float heat = agentNode.heat();
        float curiosity = agentNode.curiosity();
        float fear = agentNode.fear();

        Needs needBase = new Needs(hunger, curiosity, fear, 1, heat);

        WorldConfig worldConfig = new WorldConfig(width, height, seed, agentAmount);
        worldConfig.setAgentNeeds(needBase);
        worldConfig.setWorldId(UUID.randomUUID());
        return worldConfig;
    }

    public static PotentialLayerConfig buildPotentialLayerConfig(List<EditorNode> resolveOrder,
                                                                 int width, int height) {
        PotentialLayerConfig cfg = new PotentialLayerConfig(width, height);

        for(EditorNode node : resolveOrder) {
            switch (node.category()) {
                case "Signal" -> {
                    SignalSource signal = SignalFactory.create(node);
                    cfg.setSignalSource(signal);
                }
                case "Compositing" -> {
                    LayerStep layerStep = LayerStepFactory.create(node);
                    cfg.addLayerStep(layerStep);
                }
                case "Base Update" -> {
                    PotentialUpdater updater = PotentialUpdateFactory.create(node);
                    cfg.setPotentialUpdater(updater);
                }
                case "Time Behavior" -> {
                    TimeBehavior timeBehavior = TimeBehaviorFactory.create(node);
                    cfg.setTimeBehavior(timeBehavior);
                }
                case "Layer", "World", "Agent" -> {
                    continue;
                }
                default -> {
                    throw new RuntimeException("Unknown category: " + node.category());
                }
            }
        }
        return cfg;
    }

    public static StateLayerConfig buildStateLayerConfig(List<EditorNode> resolveOrder,
                                                         int width, int height) {
        StateLayerConfig cfg = new StateLayerConfig(width, height);

        for(EditorNode node : resolveOrder) {
            switch (node.category()) {
                case "Signal" -> {
                    SignalSource signal = SignalFactory.create(node);
                    cfg.setSignalSource(signal);
                }
                case "Compositing" -> {
                    LayerStep layerStep = LayerStepFactory.create(node);
                    cfg.addLayerStep(layerStep);
                }
                case "Base Update" -> {
                    PotentialUpdater updater = PotentialUpdateFactory.create(node);
                    cfg.setPotentialUpdater(updater);
                }
                case "State Update" -> {
                    StateUpdater updater = StateUpdateFactory.create(node);
                    cfg.setStateUpdater(updater);
                }
                case "Time Behavior" -> {
                    TimeBehavior timeBehavior = TimeBehaviorFactory.create(node);
                    cfg.setTimeBehavior(timeBehavior);
                }
                case "Layer", "World", "Agent" -> {
                    continue;
                }
                default -> {
                    throw new RuntimeException("Unknown category: " + node.category());
                }
            }
        }
        return cfg;
    }


}
