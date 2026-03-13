package com.api.service;

import com.api.resource.JsonWorldConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.sim.config.PotentialLayerConfig;
import com.sim.config.StateLayerConfig;
import com.sim.config.WorldConfig;
import com.sim.json_factories.*;
import com.sim.layer.step.LayerStep;
import com.sim.layer.time_behavior.TimeBehavior;
import com.sim.layer.update.PotentialUpdater;
import com.sim.layer.update.StateUpdater;
import com.sim.signal.SignalSource;

import java.util.ArrayList;
import java.util.List;

public class WorldConfigHandler {

    private static final SignalFactory signalFactory = new SignalFactory();
    private static final LayerStepFactory stepFactory = new LayerStepFactory();
    private static final PotentialUpdateFactory potentialFactory = new PotentialUpdateFactory();
    private static final StateUpdateFactory stateFactory = new StateUpdateFactory();
    private static final TimeBehaviorFactory timeFactory = new TimeBehaviorFactory();

    private WorldConfigHandler() {}

    public static WorldConfig translateConfig(JsonWorldConfig jsonCfg) {
        int width = jsonCfg.worldWidth();
        int height = jsonCfg.worldHeight();
        int seed = jsonCfg.seed();
        int agentCount = jsonCfg.agentCount();

        WorldConfig cfg = new WorldConfig(width, height, seed, agentCount);

        for (JsonNode layerNode : jsonCfg.layers()) {
            String id = layerNode.get("id").asText();
            String type = layerNode.get("type").asText();

            SignalSource signal = signalFactory.create(layerNode.get("signal"));
            TimeBehavior time = timeFactory.create(layerNode.get("timeBehavior"));

            // Compositing Steps
            List<LayerStep> steps = new ArrayList<>();
            for (JsonNode stepNode : layerNode.withArray("compositing")) {
                steps.add(stepFactory.create(stepNode));
            }

            // Updaters
            PotentialUpdater potentialUpdater = potentialFactory.create(layerNode.get("potentialUpdater"));
            StateUpdater stateUpdater = null;
            if (layerNode.has("stateUpdater")) {
                stateUpdater = stateFactory.create(layerNode.get("stateUpdater"));
            }

            switch (type) {
                case "PotentialLayer":
                    PotentialLayerConfig potentialCfg = new PotentialLayerConfig(width, height);
                    potentialCfg.setPotentialUpdater(potentialUpdater);
                    potentialCfg.setSignalSource(signal);
                    potentialCfg.setTimeBehavior(time);
                    potentialCfg.addCompositing(steps);
                    cfg.addPotentialLayer(id, potentialCfg);

                    break;
                case "StateLayer":
                    if (stateUpdater == null) {
                        throw new IllegalArgumentException("StateLayer needs a stateUpdater");
                    }

                    StateLayerConfig stateCfg = new StateLayerConfig(width, height);
                    stateCfg.setPotentialUpdater(potentialUpdater);
                    stateCfg.setStateUpdater(stateUpdater);
                    stateCfg.setSignalSource(signal);
                    stateCfg.setTimeBehavior(time);
                    stateCfg.addCompositing(steps);
                    cfg.addStateLayer(id, stateCfg);

                    break;
                default:
                    throw new IllegalArgumentException("Unknown layer type: " + type);
            }
        }

        return cfg;
    }
}
