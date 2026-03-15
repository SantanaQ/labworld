package com.sim.snapshot;

import com.sim.layer.LayerID;
import com.sim.world.World;
import com.sim.agent.Agent;

import java.util.*;

public class WorldSnapshot {

    private final int worldId;
    private final int width;
    private final int height;

    private final float[][] heatSnap;
    private final float[][] foodSnap;
    private final float[][] scentSnap;

    private final List<AgentSnapshot> agentSnaps;

    public WorldSnapshot(World world) {

        this.worldId = world.id();
        this.width = world.width();
        this.height = world.height();

        this.heatSnap = new float[width][height];
        this.foodSnap = new float[width][height];
        this.scentSnap = new float[width][height];
        this.agentSnaps = new ArrayList<>();

        /*LayerSnapshot heat
                = new LayerSnapshot(world.layer(LayerID.HEAT));
        this.heatSnap = heat.values();
        LayerSnapshot food = new LayerSnapshot(world.layer(LayerID.FOOD));
        this.foodSnap = food.values();
        LayerSnapshot scent = new LayerSnapshot(world.layer(LayerID.SCENT));
        this.scentSnap = scent.values();

        agentSnaps = new ArrayList<>();
        for(Agent agent : world.agents()) {
            AgentSnapshot snap = new AgentSnapshot(agent.position(), agent.velocity(),
                    agent.speed(), agent.needs());
            agentSnaps.add(snap);
        }
        */
    }

    public void refreshSnapshot() {

    }

    public int worldId() {
        return worldId;
    }

    public int agentCount() {
        return agentSnaps.size();
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

    public List<AgentSnapshot> agents() {
        return agentSnaps;
    }

    public float[][] scent() {
        return scentSnap;
    }


}
