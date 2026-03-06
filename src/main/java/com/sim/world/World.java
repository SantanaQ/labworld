package com.sim.world;

import com.sim.config.LayerConfig;
import com.sim.layers.LayerContext;
import com.sim.config.WorldConfig;
import com.sim.layers.*;
import com.sim.layers.step.LayerReferenceStep;
import com.sim.world.agent.Agent;
import com.sim.world.agent.Position;

import java.util.*;

public class World {

    final WorldConfig config;
    final LayerContext ctx;
    final Map<LayerID, LayerRuntime> runtimes;

    final Random rand;
    final List<Agent> agents;
    final OccupancyGrid occupancy;
    private float time = 1;

    public World(WorldConfig config) {
        this.ctx = new LayerContext();
        this.config = config;
        this.runtimes = new HashMap<>();

        agents = new ArrayList<>();
        occupancy = new OccupancyGrid(config.width, config.height);
        rand = new Random(config.seed);

        addRuntime(LayerID.HEAT, config.heatConfig);
        addRuntime(LayerID.FOOD, config.foodConfig);

        rebuildAll();
        spawnAgents(config.agentCount);
    }

    private void addRuntime(LayerID id, LayerConfig config) {
        LayerRuntime runtime = new LayerRuntime(id, config);
        runtimes.put(id, runtime);
    }


    public int width() {
        return config.width;
    }

    public int height() {
        return config.height;
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
            entry.getValue().layer().updatePotential(time);
        }

        for (Agent agent : agents) {
            agent.actOn(this);
        }

        for (var entry : runtimes.entrySet()) {
            WorldLayer layer = entry.getValue().layer();
            if(layer instanceof InteractiveLayer)
            {
                ((InteractiveLayer) layer).updateState();
            }
        }

        rebuildOccupancy();
        time++;
    }

    public List<Agent> agentsAt(Coordinate coord) {
        return occupancy.at(coord);
    }

    public float occupancyAt(Coordinate coord) {
        return agentsAt(coord).size();
    }

    public float time() {
        return time;
    }

    public WorldLayer layer(LayerID id) {
        return runtimes.get(id).layer();
    }

    public float layerAt(LayerID id, Coordinate coord) {
        return runtimes.get(id).layer().accessibleAt(coord);
    }

    public OccupancyGrid occupancy() {
        return occupancy;
    }

    public List<Agent> agents() {
        return agents;
    }

    public LayerContext layerContext() {
        return ctx;
    }

    private void spawnAgents(int agentCount) {
        for(int i = 0; i < agentCount; i++) {
            int x = rand.nextInt(0, config.width);
            int y = rand.nextInt(0, config.height);
            Position pos = new Position(x, y);
            Agent agent = new Agent(pos);
            agents.add(agent);
        }
    }

    public void spawnAgent(Agent agent) {
        agents.add(agent);
    }

    private void rebuildOccupancy() {
       occupancy.clear();
       for(Agent agent : agents) {
           occupancy.set(agent.position().nearestCoordinate(this), agent);
       }
    }

    public List<Coordinate> neighbors(
            Coordinate center,
            int radius
    ) {
        List<Coordinate> neighbors = new ArrayList<>();

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {

                if (dx == 0 && dy == 0) continue;

                Coordinate n = center.add(dx, dy);
                if (isInBounds(n)) {
                    neighbors.add(n);
                }
            }
        }
        return neighbors;
    }

    public boolean isInBounds(Coordinate coordinate) {
        return coordinate.x() >= 0
                && coordinate.x() < config.width
                && coordinate.y() >= 0
                && coordinate.y() < config.height;
    }


}
