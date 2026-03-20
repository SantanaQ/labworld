package com.api.service;

import com.sim.config.WorldConfig;
import com.sim.snapshot.WorldSnapshot;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;
import java.util.zip.Deflater;

public class FrameEncoder {

    private final ByteBuffer buffer;

    public FrameEncoder(WorldConfig config) {
        // Header (UUID=16, Ints=8) + 3 Layer (1 Byte/Pixel) + Agents
        int layerSize = config.width() * config.height();
        int agentSize = config.agentCount() * 22; // x,y(float), vX,vY (float),speed,energy,4 needs(byte)
        int totalSize = 16 + 8 + (3 * layerSize) + agentSize;

        this.buffer = ByteBuffer
                .allocateDirect(totalSize)
                .order(ByteOrder.LITTLE_ENDIAN);
    }

    public ByteBuffer encode(WorldSnapshot snap) {
        buffer.clear();

        // Header
        UUID uuid = snap.worldId();
        buffer.putLong(uuid.getMostSignificantBits());
        buffer.putLong(uuid.getLeastSignificantBits());
        buffer.putInt(snap.width());
        buffer.putInt(snap.height());

        // Layers (0.0 - 1.0 float -> unsigned byte)
        encodeLayer(snap.heat());
        encodeLayer(snap.food());
        encodeLayer(snap.scent());

        // Agents
        float[] agents = snap.agents();
        for (int i = 0; i < agents.length; i += WorldSnapshot.AGENT_PROPS) {
            // Position (pX, py) as short
            buffer.putFloat(agents[i]);     // posX
            buffer.putFloat(agents[i + 1]); // posY

            // Velocity (vX, vY)
            buffer.putFloat(agents[i + 2]);
            buffer.putFloat(agents[i + 3]);

            // Werte 0.0 - 1.0 als Byte
            buffer.put((byte) (agents[i + 4] * 255)); // speed
            buffer.put((byte) (agents[i + 5] * 255)); // energy
            buffer.put((byte) (agents[i + 6] * 255)); // hunger
            buffer.put((byte) (agents[i + 7] * 255)); // heat
            buffer.put((byte) (agents[i + 8] * 255)); // curiosity
            buffer.put((byte) (agents[i + 9] * 255)); // fear


        }

        buffer.flip();
        return buffer;
    }

    private void encodeLayer(float[] data) {
        for (float f : data) {
            int val = (int) (f * 255.0f);
            if (val < 0) val = 0;
            if (val > 255) val = 255;
            buffer.put((byte) val);
        }
    }
}
