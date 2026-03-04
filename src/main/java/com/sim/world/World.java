package com.sim.world;

import com.sim.config.WorldConfig;
import com.sim.layers.*;
import com.sim.layers.step.Normalize;
import com.sim.layers.step.SuitabilityMask;
import com.sim.layers.time_behavior.Composite;
import com.sim.layers.time_behavior.DomainWarp;
import com.sim.layers.time_behavior.TimeBehavior;
import com.sim.noise.FractalNoise;
import com.sim.noise.ValueNoise;
import com.sim.layers.step.SoftThreshold;
import com.sim.layers.time_behavior.Drifting;
import com.sim.world.agent.Agent;
import com.sim.world.agent.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {

    /*final*/ int width;
    /*final*/ int height;

     int seed = 9992221;
    Random rand;

    int agentCount = 5;
    List<Agent> agents;
    OccupancyGrid occupancy;

    WorldLayer slope;
    ProceduralLayer temperature;
    InteractiveLayer food;
    WorldLayer stress;
    WorldLayer scent;
    List<WorldLayer> layers;

    float time = 1;

    /*final*/ WorldConfig config;

    public World(WorldConfig config) {
        this.config = config;
        this.temperature = config.temperatureConfig.buildLayer();
    }

    public World(int width, int height) {
        this.width = width;
        this.height = height;
        agents = new ArrayList<>();
        occupancy = new OccupancyGrid(width, height);
        layers = new ArrayList<>();

        rand = new Random(seed);

        TimeBehavior drift = new Drifting(0.1f, 2.1415f);

        TimeBehavior warp = new DomainWarp(
                new FractalNoise(new ValueNoise(seed+1, 64), 3, 0.6f),
                new FractalNoise(new ValueNoise(seed+2, 64), 3, 0.6f),
                50f
        );

        TimeBehavior time = new Composite(
                List.of(drift, warp)
        );

        temperature = new LayerBuilder(width, height)
                .withSignalSource(new FractalNoise(seed+3, 50, 2,5))
                .withTimeBehaviour(time)
                .step(new SoftThreshold(0.2f, 0.1f))
                .step(new Normalize(0, 1))
                .buildProceduralLayer();

        food = new LayerBuilder(width, height)
                .withSignalSource(new FractalNoise(seed+100, 70, 2,5))
                .withTimeBehaviour(drift)
                .step(new SoftThreshold(0.7f, 0.1f))
                .step(new SuitabilityMask(temperature, 0.5f, 0.7f))
                .step(new Normalize(0, 1))
                .buildInteractiveLayer();

        layers.addAll(List.of(temperature, food));

        spawnAgents();

    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public void tick() {
        for (WorldLayer layer : layers) {
            layer.updatePotential(time);
        }

        for (Agent agent : agents) {
            agent.actOn(this);
        }

        for (WorldLayer layer : layers) {
            if(layer instanceof InteractiveLayer)
            {
                ((InteractiveLayer) layer).updateState();
            }
        }

        rebuildOccupancy();
        time++;
    }

    public float tempAt(Coordinate coord) {
        return temperature.accessibleAt(coord);
    }

    public float slopeAt(Coordinate coord) {
        return slope.accessibleAt(coord);
    }

    public float foodAt(Coordinate coord) {
        return food.accessibleAt(coord);
    }

    public float stressAt(Coordinate coord) {
        return stress.accessibleAt(coord);
    }

    public float scentAt(Coordinate coord) {
        return scent.accessibleAt(coord);
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

    public ProceduralLayer temperature() {
        return temperature;
    }

    public InteractiveLayer food() {
        return food;
    }

    public OccupancyGrid occupancy() {
        return occupancy;
    }

    public List<Agent> agents() {
        return agents;
    }

    private void spawnAgents() {
        for(int i = 0; i < agentCount; i++) {
            int x = rand.nextInt(0, width);
            int y = rand.nextInt(0, height);
            Position pos = new Position(x, y);
            Agent agent = new Agent(pos);
            agents.add(agent);
        }
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
                && coordinate.x() < width
                && coordinate.y() >= 0
                && coordinate.y() < height;
    }


}
