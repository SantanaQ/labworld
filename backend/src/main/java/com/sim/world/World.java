package com.sim.world;

import com.sim.agent.Needs;
import com.sim.config.LayerConfig;
import com.sim.layer.LayerContext;
import com.sim.config.WorldConfig;
import com.sim.layer.*;
import com.sim.layer.step.LayerReferenceStep;
import com.sim.agent.Agent;
import com.sim.agent.Position;
import com.sim.utils.MathHelpers;

import java.util.*;

public class World {

    final WorldConfig config;
    final LayerContext ctx;
    final Map<LayerID, LayerRuntime> runtimes;



    final Random rand;
    final List<Agent> agents;
    private float time = 1;

    public World(WorldConfig config) {
        this.ctx = new LayerContext();
        this.config = config;
        this.runtimes = new HashMap<>();

        agents = new ArrayList<>();
        rand = new Random(config.seed());

        addRuntime(LayerID.HEAT, config.heatConfig);
        addRuntime(LayerID.SUPPLY, config.supplyConfig);
        addRuntime(LayerID.SCENT, config.scentConfig);

        rebuildAll();
        spawnAgents(config.agentCount(), config.agentNeeds());
    }

    private void addRuntime(LayerID id, LayerConfig config) {
        LayerRuntime runtime = new LayerRuntime(id, config);
        runtimes.put(id, runtime);
    }

    public UUID id() {
        return config.worldId();
    }

    public int width() {
        return config.width();
    }

    public int height() {
        return config.height();
    }

    public void rebuildAll() {
        ctx.clear();

        LayerDependencyGraph graph = new LayerDependencyGraph(runtimes);
        List<LayerID> order = graph.topologicalSort();
        for (LayerID id : order) {
            rebuildLayer(id);
        }
    }

    public void rebuildLayer(LayerID id) {
        LayerRuntime runtime = runtimes.get(id);
        for(var ref : runtime.layerSteps()) {
            if(ref instanceof LayerReferenceStep) {
                ((LayerReferenceStep) ref).resolve(ctx);
            }
        }
        runtime.updateLayer(runtime.config().build());
        WorldLayer layer = runtime.layer();
        ctx.register(id, layer);
        runtime.config().clearDirty();
    }

    public void tick() {
        for (var entry : runtimes.entrySet()) {
            ((PotentialLayer)entry.getValue().layer()).updatePotential(time);
        }

        for (Agent agent : agents) {
            agent.actOn(this);
        }

        for (var entry : runtimes.entrySet()) {
            WorldLayer layer = entry.getValue().layer();
            if(layer instanceof StateLayer)
            {
                ((StateLayer) layer).updateState();
            }
        }

        time++;
    }

    public int agentCount() {
        return agents.size();
    }

    public float time() {
        return time;
    }

    public WorldLayer layer(LayerID id) {
        return runtimes.get(id).layer();
    }

    public float layerAt(LayerID id, Coordinate coord) {
        return runtimes.get(id).layer().valueAt(coord.x(), coord.y());
    }

    public List<Agent> agents() {
        return agents;
    }

    public LayerContext layerContext() {
        return ctx;
    }

    public Random random() {
        return rand;
    }

    public Map<LayerID, LayerRuntime>  runtimes() {
        return runtimes;
    }

    private void spawnAgents(int agentCount, Needs agentNeeds) {
        for(int i = 0; i < agentCount; i++) {
            int x = rand.nextInt(0, config.width());
            int y = rand.nextInt(0, config.height());
            Position pos = new Position(x, y);
            Agent agent = new Agent(pos, agentNeeds.copy());
            agents.add(agent);
        }
    }

    public void affect(LayerID layerId, Coordinate c, float amount) {
        WorldLayer layer = layer(layerId);
        if(layer instanceof StateLayer) {
            ((StateLayer) layer).applyInfluence(c.x(), c.y(), amount);
        }
    }

    public void affectKernel(LayerID layerId, Coordinate c) {
        StateLayer layer = (StateLayer) layer(layerId);
        float[][] gaussian = MathHelpers.gaussian3x3;
        int center = 1;
        for(int dy = -1; dy <= 1; dy++) {
            for(int dx = -1; dx <= 1; dx++) {
                float val = -(layer.valueAt(c.x() + dx,c.y() + dy)
                        * gaussian[center + dy][center + dx]);
                layer.applyInfluence(c.x() + dx, c.y() + dy, val);
            }
        }
    }



}
