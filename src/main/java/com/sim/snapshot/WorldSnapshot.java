package com.sim.snapshot;

import com.sim.layer.LayerID;
import com.sim.layer.Renderable;
import com.sim.world.World;
import com.sim.agent.Agent;

import java.util.*;

public class WorldSnapshot {

    private final int width;
    private final int height;

    private final float[][] heatSnap;
    private final float[][] foodSnap;
    private final float[][] scentSnap;

    private final int[][] occupancySnap;
    private final List<AgentSnapshot> agentSnaps;

    public WorldSnapshot(World world) {

        this.width = world.width();
        this.height = world.height();

        LayerSnapshot heat
                = new LayerSnapshot((Renderable) world.layer(LayerID.HEAT));
        this.heatSnap = heat.values();
        LayerSnapshot food = new LayerSnapshot((Renderable) world.layer(LayerID.FOOD));
        this.foodSnap = food.values();
        LayerSnapshot scent = new LayerSnapshot((Renderable) world.layer(LayerID.SCENT));
        this.scentSnap = scent.values();

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

    public float[][] heat() {
        return heatSnap;
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


    public float[][] scent() {
        return scentSnap;
    }
}
