package com.api.encoding;

import com.sim.layer.LayerID;
import com.sim.snapshot.WorldSnapshot;

import java.nio.ByteBuffer;

public interface LayerChunk extends Chunk {

    int maxBytes(int worldWidth, int worldHeight);
    //int totalBytes();
    void encode(LayerID layerID, ChunkID chunkID, ByteBuffer buffer, WorldSnapshot snapshot);

}
