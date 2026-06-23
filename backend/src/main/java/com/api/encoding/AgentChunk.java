package com.api.encoding;

import com.sim.snapshot.AgentProps;
import com.sim.snapshot.WorldSnapshot;

import java.nio.ByteBuffer;

public class AgentChunk implements Chunk {

    private static final int STRIDE = AgentProps.totalBytes();

    public int totalBytes(int agentCount) {
        return CHUNK_IDENTIFICATION_BYTES + agentCount * STRIDE;
    }

    public void encode(ChunkID chunkID, ByteBuffer buffer, WorldSnapshot snapshot) {
        int chunkSize = snapshot.agentCount() * STRIDE;
        System.out.println(chunkSize);

        // header: id, size of data, stride of one data entry
        encodeChunkHeader(buffer, chunkID, chunkSize, (byte) STRIDE);

        // data
        float[] agentData = snapshot.agents();
        for (int i = 0; i < agentData.length; i += WorldSnapshot.AGENT_PROPS) {
            // ID
            buffer.putShort((short) agentData[i + AgentProps.ID.ordinal()]);

            // Position (pX, py)
            buffer.putFloat(agentData[i + AgentProps.X.ordinal()]);
            buffer.putFloat(agentData[i + AgentProps.Y.ordinal()]);

            // Velocity (vX, vY)
            buffer.putFloat(agentData[i + AgentProps.VX.ordinal()]);
            buffer.putFloat(agentData[i + AgentProps.VY.ordinal()]);

            // Speed
            encodeAsByte(buffer, agentData[i + AgentProps.SPEED.ordinal()]);
        }
    }


}
