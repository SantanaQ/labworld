package com.sim.snapshot;

import com.sim.agent.Agent;
import com.sim.layer.LayerID;
import com.sim.world.World;

import java.util.*;

public class WorldSnapshot {

    public static int AGENT_PROPS = AgentProps.values().length;

    private static final float CHANGE_THRESHOLD = 0.005f;

    private final UUID worldId;
    private final int width;
    private final int height;

    private final int[] indexBuffer;
    private final float[] deltaBuffer;

    private final float[] lastHeat;
    private final float[] lastFood;
    private final float[] lastScent;

    private final LayerDelta heatDelta;
    private final LayerDelta foodDelta;
    private final LayerDelta scentDelta;

    private final List<float[]> agentSnaps;

    private final float[] agentBuffer;

    public WorldSnapshot(World world) {

        this.worldId = world.id();
        this.width = world.width();
        this.height = world.height();

        this.indexBuffer = new int[width * height];
        this.deltaBuffer = new float[width * height];

        this.heatDelta = new LayerDelta();
        this.foodDelta = new LayerDelta();
        this.scentDelta = new LayerDelta();

        this.lastHeat = new float[width * height];
        this.lastFood = new float[width * height];
        this.lastScent = new float[width * height];

        this.agentBuffer = new float[world.agentCount() *  AGENT_PROPS];

        this.agentSnaps = new ArrayList<>();
    }

    public void refresh(World world) {
        agentSnaps.clear();

        float[] currentFood = world.layer(LayerID.SUPPLY).values();
        float[] currentScent = world.layer(LayerID.SCENT).values();
        float[] currentHeat = world.layer(LayerID.HEAT).values();

        computeDelta(foodDelta, lastFood, currentFood);
        computeDelta(scentDelta, lastScent, currentScent);
        computeDelta(heatDelta, lastHeat, currentHeat);

        copyLayer(currentFood, lastFood);
        copyLayer(currentScent, lastScent);
        copyLayer(currentHeat, lastHeat);

        int base = 0;
        for(Agent agent : world.agents()) {
            agentBuffer[base + AgentProps.ID.ordinal()] = agent.id();
            agentBuffer[base + AgentProps.X.ordinal()] = agent.position().x();
            agentBuffer[base + AgentProps.Y.ordinal()] = agent.position().y();
            agentBuffer[base + AgentProps.VX.ordinal()] = agent.velocity().vx();
            agentBuffer[base + AgentProps.VY.ordinal()] = agent.velocity().vy();
            agentBuffer[base + AgentProps.SPEED.ordinal()] = agent.speed();
            agentBuffer[base + AgentProps.ENERGY.ordinal()] = agent.needs().energy();
            agentBuffer[base + AgentProps.HUNGER.ordinal()] = agent.needs().hunger();
            agentBuffer[base + AgentProps.HEAT.ordinal()] = agent.needs().heat();
            agentBuffer[base + AgentProps.CURIOSITY.ordinal()] = agent.needs().curiosity();
            agentBuffer[base + AgentProps.FEAR.ordinal()] = agent.needs().fear();
            base += AGENT_PROPS;
        }
    }

    private void copyLayer(float[] values,  float[] buffer) {
        System.arraycopy(values, 0, buffer, 0, values.length);
    }

    private void computeDelta(LayerDelta delta, float[] lastValues, float[] currentValues) {
        int count = 0;
        for(int i = 0; i < currentValues.length; i++) {

            if(Math.abs(lastValues[i] - currentValues[i]) > CHANGE_THRESHOLD) {
                indexBuffer[count] = i;
                deltaBuffer[count++] = currentValues[i];
            }
        }

        delta.setSize(count);
        delta.setValues(Arrays.copyOf(deltaBuffer, count));
        delta.setIndexes(Arrays.copyOf(indexBuffer, count));
    }

    public UUID worldId() {
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
        return lastHeat;
    }

    public float[] food() {
        return lastFood;
    }

    public float[] scent() {
        return lastScent;
    }

    public LayerDelta heatDelta() {
        return heatDelta;
    }

    public LayerDelta foodDelta() {
        return foodDelta;
    }

    public LayerDelta scentDelta() {
        return scentDelta;
    }

    public float[] agents() {
        return agentBuffer;
    }

}
