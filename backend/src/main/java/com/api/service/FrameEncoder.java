package com.api.service;

import com.sim.config.WorldConfig;
import com.sim.snapshot.AgentProps;
import com.sim.snapshot.LayerDelta;
import com.sim.snapshot.WorldSnapshot;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;

/*TODO: refactor to chunk system
    HEADER: id, width, height,
    CHUNK: type, size, stride, payload
 */
public class FrameEncoder {

    private final ByteBuffer buffer;

    public FrameEncoder(WorldConfig config) {
        int layerSize = config.width() * config.height();
        int agentSize = config.agentCount() * AgentProps.totalBytes();
        //int totalSize = 16 + 8 + (3 * layerSize) + agentSize;

        int worstLayer = 5 * layerSize;
        int totalSize = 25 + (3 * worstLayer) + agentSize;

        this.buffer = ByteBuffer
                .allocateDirect(totalSize)
                .order(ByteOrder.LITTLE_ENDIAN);
    }

    public ByteBuffer encode(WorldSnapshot snap, boolean fullFrame) {
        buffer.clear();

        // Header
        UUID uuid = snap.worldId();
        buffer.putLong(uuid.getMostSignificantBits());
        buffer.putLong(uuid.getLeastSignificantBits());
        buffer.putInt(snap.width());
        buffer.putInt(snap.height());
        buffer.put((byte) (fullFrame ? 1 : 0));

        // Layers (0.0 - 1.0 float -> unsigned byte)
        if(fullFrame) {
            encodeLayer(snap.heat());
            encodeLayer(snap.food());
            encodeLayer(snap.scent());
        } else {
            encodeLayerDelta(snap.heatDelta());
            encodeLayerDelta(snap.foodDelta());
            encodeLayerDelta(snap.scentDelta());
        }

        // Agent Stride
        buffer.put((byte) AgentProps.totalBytes());

        // Agents
        float[] agents = snap.agents();
        for (int i = 0; i < agents.length; i += WorldSnapshot.AGENT_PROPS) {
            // ID
            buffer.putShort((short) agents[i + AgentProps.ID.ordinal()]);

            // Position (pX, py)
            buffer.putFloat(agents[i + AgentProps.X.ordinal()]);    // posX
            buffer.putFloat(agents[i + AgentProps.Y.ordinal()]);    // posY

            // Velocity (vX, vY)
            buffer.putFloat(agents[i + AgentProps.VX.ordinal()]);
            buffer.putFloat(agents[i + AgentProps.VY.ordinal()]);

            // Werte 0.0 - 1.0 als Byte
            buffer.put((byte) (agents[i + AgentProps.SPEED.ordinal()] * 255)); // speed
            buffer.put((byte) (agents[i + AgentProps.ENERGY.ordinal()] * 255)); // energy
            buffer.put((byte) (agents[i + AgentProps.HUNGER.ordinal()] * 255)); // hunger
            buffer.put((byte) (agents[i + AgentProps.HEAT.ordinal()] * 255)); // heat
            buffer.put((byte) (agents[i + AgentProps.CURIOSITY.ordinal()] * 255)); // curiosity
            buffer.put((byte) (agents[i + AgentProps.FEAR.ordinal()] * 255)); // fear


        }

        buffer.flip();
        return buffer;
    }

    private void encodeLayer(float[] data) {
        for (float f : data) {
            encodeAsByte(f);
        }
    }

    private void encodeLayerDelta(LayerDelta layerDelta) {
        int size = layerDelta.size();
        int[] indexes = layerDelta.indexes();
        float[] values = layerDelta.values();
        buffer.putInt(size);
        for(int i = 0; i < size; i++) {
            buffer.putInt(indexes[i]);
            encodeAsByte(values[i]);
        }
    }

    private void encodeAsByte(float value) {
        value = (int) (value * 255.0f);
        if (value < 0) value = 0;
        if (value > 255) value = 255;
        buffer.put((byte) value);
    }

}
