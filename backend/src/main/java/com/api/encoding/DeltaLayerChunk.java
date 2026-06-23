package com.api.encoding;

import com.sim.layer.LayerID;
import com.sim.snapshot.LayerDelta;
import com.sim.snapshot.WorldSnapshot;

import java.nio.ByteBuffer;

public class DeltaLayerChunk implements LayerChunk {

    private static final int INDEX_SIZE = Integer.BYTES;
    private static final int VALUE_SIZE = Byte.BYTES;

    private static final int STRIDE = INDEX_SIZE + VALUE_SIZE;

    @Override
    public int maxBytes(int worldWidth, int worldHeight) {
        int layerSize = worldWidth * worldHeight;
        int worstCaseDataSize = layerSize * INDEX_SIZE + layerSize * VALUE_SIZE;

        return CHUNK_IDENTIFICATION_BYTES + worstCaseDataSize;
    }

    @Override
    public void encode(LayerID layerID, ChunkID chunkID, ByteBuffer buffer, WorldSnapshot snapshot) {
        int valueCount = snapshot.deltaOf(layerID).size();
        int chunkSize = valueCount * INDEX_SIZE + valueCount * VALUE_SIZE;

        // header: id, size of data, stride of one data entry
        encodeChunkHeader(buffer, chunkID, chunkSize, (byte) STRIDE);

        // chunk data
        LayerDelta delta = snapshot.deltaOf(layerID);
        int[] indexes = delta.indexes();
        float[] values = delta.values();

        for(int i = 0; i < valueCount; i++) {
            buffer.putInt(indexes[i]);
            encodeAsByte(buffer, values[i]);
        }
    }
}
