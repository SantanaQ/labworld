package com.api.encoding;

import java.nio.ByteBuffer;

public interface Chunk {

    int CHUNK_ID_SIZE = Byte.BYTES;
    int CHUNK_SIZE_SIZE = Integer.BYTES;
    int STRIDE_SIZE = Byte.BYTES;

    int CHUNK_IDENTIFICATION_BYTES = CHUNK_ID_SIZE + CHUNK_SIZE_SIZE + STRIDE_SIZE;

    default void encodeAsByte(ByteBuffer buffer, float value) {
        value = (int) (value * 255.0f);
        if (value < 0) value = 0;
        if (value > 255) value = 255;
        buffer.put((byte) value);
    }

    default void encodeChunkHeader(ByteBuffer buffer, ChunkID chunkID, int chunkSize, byte stride) {
        // chunk id
        buffer.put(chunkID.id);

        // size of data in byte
        buffer.putInt(chunkSize);

        // size of one data entry
        buffer.put(stride);
    }

}
