package com.sim.snapshot;

import com.sim.agent.Agent;
import com.sim.layer.LayerID;
import com.sim.world.World;

import java.util.*;

public class WorldSnapshot {

    public static int AGENT_PROPS = AgentProps.values().length;

    private static final float CHANGE_THRESHOLD = 0.003f;

    private final UUID worldId;
    private final int width;
    private final int height;
    private final int layerCount;

    private final int[] indexBuffer;
    private final float[] deltaBuffer;

    private final HashMap<LayerID, float[]> layerValues;
    private final HashMap<LayerID, LayerDelta> layerDeltas;

    private final float[] agentBuffer;
    private final int agentCount;

    public WorldSnapshot(World world) {

        this.worldId = world.id();
        this.width = world.width();
        this.height = world.height();
        this.layerCount = world.layerCount();
        this.agentCount = world.agentCount();

        this.indexBuffer = new int[width * height];
        this.deltaBuffer = new float[width * height];

        this.layerValues = new HashMap<>();
        this.layerDeltas = new HashMap<>();
        for(LayerID id : LayerID.values()) {
            layerValues.put(id, new float[width * height]);
            layerDeltas.put(id, new LayerDelta());
        }

        this.agentBuffer = new float[world.agentCount() *  AGENT_PROPS];
    }

    public void refresh(World world) {
        for (LayerID id : LayerID.values()) {
            float[] currentValues = world.layer(id).values();
            // first record values change
            computeDelta(id, currentValues);
            // then update full frame buffer
            copyLayer(id, currentValues);
        }

        int base = 0;
        for(Agent agent : world.agents()) {
            agentBuffer[base + AgentProps.ID.ordinal()] = agent.id();
            agentBuffer[base + AgentProps.X.ordinal()] = agent.position().x();
            agentBuffer[base + AgentProps.Y.ordinal()] = agent.position().y();
            agentBuffer[base + AgentProps.VX.ordinal()] = agent.velocity().vx();
            agentBuffer[base + AgentProps.VY.ordinal()] = agent.velocity().vy();
            agentBuffer[base + AgentProps.SPEED.ordinal()] = agent.speed();
            base += AGENT_PROPS;
        }
    }

    private void copyLayer(LayerID id,  float[] values) {
        float[] buffer = layerValues.get(id);
        System.arraycopy(values, 0, buffer, 0, values.length);
    }

    private void computeDelta(LayerID id, float[] currentValues) {
        LayerDelta delta = layerDeltas.get(id);
        float[] lastValues = layerValues.get(id);
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
        return agentCount;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public float[] valuesOf(LayerID id) {
        return layerValues.get(id);
    }

    public LayerDelta deltaOf(LayerID id) {
        return layerDeltas.get(id);
    }

    public int layerCount() {
        return layerCount;
    }

    public float[] agents() {
        return agentBuffer;
    }

}
