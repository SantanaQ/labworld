package com.sim.snapshot;

import com.sim.world.Coordinate;
import com.sim.world.World;
import com.sim.world.agent.Agent;

import java.util.*;

public class WorldSnapshot {

    private final int width;
    private final int height;

    private final float[][] tempSnap;
    private final float[][] foodSnap;

    private final int[][] occupancySnap;
    //private final Map<Coordinate, AgentSnapshot> agentSnaps;
    private final List<AgentSnapshot> agentSnaps;

    public WorldSnapshot(World world) {

        this.width = world.width();
        this.height = world.height();

        LayerSnapshot temperature = new LayerSnapshot(world.temperature());
        this.tempSnap = temperature.values();
        LayerSnapshot food = new LayerSnapshot(world.food());
        this.foodSnap = food.values();

        OccupancySnapshot occupancy = new OccupancySnapshot(world.occupancy(), width, height);
        occupancySnap = occupancy.values();

        agentSnaps = new ArrayList<>();
        for(Agent agent : world.agents()) {
            AgentSnapshot snap = new AgentSnapshot(agent.position(), agent.velocity(), agent.speed());
            agentSnaps.add(snap);
        }

    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public float[][] temperature() {
        return tempSnap;
    }

    public float[][] food() {
        return foodSnap;
    }

    public int[][] occupancy() {
        return occupancySnap;
    }

    public List<AgentSnapshot> agents() {
        return agentSnaps;
    }




}
