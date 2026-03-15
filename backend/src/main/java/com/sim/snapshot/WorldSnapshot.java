package com.sim.snapshot;

import com.sim.agent.Agent;
import com.sim.layer.LayerID;
import com.sim.world.World;

import java.util.*;

public class WorldSnapshot {

    public static int AGENT_PROPS = AgentProps.values().length;

    private final int worldId;
    private final int width;
    private final int height;

    private final float[] heatBuffer;
    private final float[] foodBuffer;
    private final float[] scentBuffer;

    private final List<float[]> agentSnaps;

    private final float[] agentBuffer;

    public WorldSnapshot(World world) {

        this.worldId = world.id();
        this.width = world.width();
        this.height = world.height();

        this.heatBuffer = new float[width * height];
        this.foodBuffer = new float[width * height];
        this.scentBuffer = new float[width * height];

        this.agentBuffer = new float[world.agentCount() *  AGENT_PROPS];

        this.agentSnaps = new ArrayList<>();
    }

    public void refresh(World world) {
        agentSnaps.clear();

        copyLayer(world, LayerID.FOOD, foodBuffer);
        copyLayer(world, LayerID.SCENT, scentBuffer);
        copyLayer(world, LayerID.HEAT, heatBuffer);

        int base = 0;
        for(Agent agent : world.agents()) {
            agentBuffer[base] = agent.position().x();
            agentBuffer[base + 1] = agent.position().y();
            agentBuffer[base + 2] = agent.velocity().vx();
            agentBuffer[base + 3] = agent.velocity().vy();
            agentBuffer[base + 4] = agent.speed();
            agentBuffer[base + 5] = agent.needs().energy();
            agentBuffer[base + 6] = agent.needs().hunger();
            agentBuffer[base + 7] = agent.needs().heat();
            agentBuffer[base + 8] = agent.needs().curiosity();
            agentBuffer[base + 9] = agent.needs().fear();
            base += AGENT_PROPS;
        }
    }

    private void copyLayer(World world, LayerID layerId, float[] buffer) {
        float[] src = world.layer(layerId).values();
        System.arraycopy(src, 0, buffer, 0, src.length);
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

    public float[] heat() {
        return heatBuffer;
    }

    public float[] food() {
        return foodBuffer;
    }

    public float[] agents() {
        return agentBuffer;
    }

    public float[] scent() {
        return scentBuffer;
    }


}
