package com.api.service;

import com.api.service.frame_layouts.WorldLayout;
import com.sim.snapshot.AgentSnapshot;
import com.sim.snapshot.WorldSnapshot;

public class FrameEncoder {

    private final byte[] frame;

    public FrameEncoder(WorldLayout layout) {
        frame = new byte[layout.totalSize];
    }

    public byte[] encode(WorldSnapshot snap) {
        int offset = 0;
        offset = writeIntLE(offset, snap.worldId());
        offset = writeIntLE(offset, snap.width());
        offset = writeIntLE(offset, snap.height());
        offset = encodeLayers(snap, offset);
        encodeAgents(snap, offset);

        return frame;
    }

    private int encodeLayers(WorldSnapshot snap, int offset) {
        offset = encodeLayerGrid(snap.heat(), offset);
        offset = encodeLayerGrid(snap.food(), offset);
        offset = encodeLayerGrid(snap.scent(), offset);
        return offset;
    }

    private int encodeLayerGrid(float[][] grid, int offset) {
        for (float[] floats : grid) {
            for (float aFloat : floats) {
                writeFloatLE(offset, aFloat);
                offset += Float.BYTES;
            }
        }
        return offset;
    }

    private int encodeAgents(WorldSnapshot snap, int offset) {
        for (AgentSnapshot agent : snap.agents()) {
            writeFloatLE(offset, agent.position().x()); offset += Float.BYTES;
            writeFloatLE(offset, agent.position().y()); offset += Float.BYTES;

            writeFloatLE(offset, agent.velocity().vx()); offset += Float.BYTES;
            writeFloatLE(offset, agent.velocity().vy()); offset += Float.BYTES;

            writeFloatLE(offset, agent.speed()); offset += Float.BYTES;

            for (float need : agent.needs()) {
                writeFloatLE(offset, need);
                offset += Float.BYTES;
            }
        }
        return offset;
    }

    public byte[] currentFrame() {
        return frame;
    }

    public int writeIntLE(int offset, int value) {
        frame[offset++] = (byte)(value & 0xFF);
        frame[offset++] = (byte)((value >> 8) & 0xFF);
        frame[offset++] = (byte)((value >> 16) & 0xFF);
        frame[offset++] = (byte)((value >> 24) & 0xFF);
        return offset;
    }

    private void writeFloatLE(int offset, float value) {
        int bits = Float.floatToIntBits(value);
        frame[offset] = (byte)(bits & 0xFF);
        frame[offset+1] = (byte)((bits >> 8) & 0xFF);
        frame[offset+2] = (byte)((bits >> 16) & 0xFF);
        frame[offset+3] = (byte)((bits >> 24) & 0xFF);
    }

}
