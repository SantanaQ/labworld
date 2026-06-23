package com.api.encoding;

import com.sim.layer.LayerID;
import com.sim.snapshot.WorldSnapshot;

import java.nio.ByteBuffer;

public class FullLayerChunk implements LayerChunk {

    private static final int VALUE_SIZE = Byte.BYTES;
    private static final int STRIDE = Byte.BYTES; // quantized grid value

    @Override
    public int maxBytes(int worldWidth, int worldHeight) {
        int layerSize = worldWidth * worldHeight;
        int chunkSize = layerSize * VALUE_SIZE;

        return CHUNK_IDENTIFICATION_BYTES + chunkSize;
    }

    @Override
    public void encode(LayerID layerID, ChunkID chunkID, ByteBuffer buffer, WorldSnapshot snapshot) {
        int layerSize = snapshot.width() * snapshot.height();
        int chunkSize = layerSize * VALUE_SIZE;

        // header: id, size of data, stride of one data entry
        encodeChunkHeader(buffer, chunkID, chunkSize, (byte) STRIDE);

        // chunk data
        float[] layerData = snapshot.valuesOf(layerID);
        for(float value : layerData) {
            encodeAsByte(buffer, value);
        }
    }
}
