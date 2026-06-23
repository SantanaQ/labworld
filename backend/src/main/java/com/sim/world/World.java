package com.sim.world;

import com.sim.agent.Needs;
import com.sim.config.LayerConfig;
import com.sim.layer.LayerContext;
import com.sim.config.WorldConfig;
import com.sim.layer.*;
import com.sim.layer.step.LayerReferenceStep;
import com.sim.agent.Agent;
import com.sim.agent.Position;

import java.util.*;

public class World {

    final WorldConfig config;
    final LayerContext ctx;
    final Map<LayerID, LayerRuntime> runtimes;
    final OccupancyGrid occupancyGrid;

    final Random rand;
    final List<Agent> agents;
    private float time = 1;

    private int currentTick = 1;

    public World(WorldConfig config) {
        this.ctx = new LayerContext();
        this.config = config;
        this.runtimes = new HashMap<>();
        this.occupancyGrid = new OccupancyGrid(config.width, config.height);

        agents = new ArrayList<>();
        rand = new Random(config.seed());

        addRuntime(LayerID.HEAT, config.heatConfig);
        addRuntime(LayerID.SUPPLY, config.supplyConfig);
        addRuntime(LayerID.SCENT, config.scentConfig);
        addRuntime(LayerID.TRAIL, config.trailConfig);
        addRuntime(LayerID.STRESS, config.stressConfig);

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
            ((PotentialLayer) entry.getValue().layer()).updatePotential(time);
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

        occupancyGrid.refresh(this, agents);

        time++;
        currentTick++;
    }

    public int agentCount() {
        return agents.size();
    }

    public float time() {
        return time;
    }

    public int currentTick() {
        return currentTick;
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

    public OccupancyGrid occupancyGrid() {
        return occupancyGrid;
    }

    public Random random() {
        return rand;
    }

    public int layerCount() {
        return config.layerCount();
    }


    private void spawnAgents(int agentCount, Needs agentNeeds) {
        short id =  1;
        for(int i = 0; i < agentCount; i++) {
            int x = rand.nextInt(0, config.width());
            int y = rand.nextInt(0, config.height());
            Position pos = new Position(x, y);
            Agent agent = new Agent(id, pos, agentNeeds.copy());
            agents.add(agent);
            id++;
        }
    }

    public void affect(LayerID layerId, Coordinate c, float amount) {
        WorldLayer layer = layer(layerId);

        if(!layer.hasState()) {
            throw new IllegalArgumentException("Layer: " + layerId.name() + " has no state");
        }

        ((StateLayer) layer).applyInfluence(c.x(), c.y(), amount);
    }


}
