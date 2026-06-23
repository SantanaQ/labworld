package com.api.encoding;

import com.sim.snapshot.WorldSnapshot;

import java.nio.ByteBuffer;
import java.util.UUID;

public class FrameHeader {

    private static final int UUID_SIZE = Long.BYTES * 2;
    private static final int WIDTH_SIZE = Integer.BYTES;
    private static final int HEIGHT_SIZE = Integer.BYTES;
    private static final int ENCODING_TYPE_SIZE = Byte.BYTES;
    private static final int CHUNK_COUNT_SIZE = Byte.BYTES;
    private static final int VERSION_SIZE = Byte.BYTES;

    private static final int NON_LAYER_CHUNKS = 1; // agents

    public int totalBytes() {
        return VERSION_SIZE
                + UUID_SIZE
                + WIDTH_SIZE
                + HEIGHT_SIZE
                + ENCODING_TYPE_SIZE
                + CHUNK_COUNT_SIZE;
    }

    public void encode(ByteBuffer buffer, WorldSnapshot snapshot, byte protocolVersion, boolean fullFrame) {
        // version
        buffer.put(protocolVersion);

        // id
        UUID uuid = snapshot.worldId();
        buffer.putLong(uuid.getMostSignificantBits());
        buffer.putLong(uuid.getLeastSignificantBits());

        // world dimensions
        buffer.putInt(snapshot.width());
        buffer.putInt(snapshot.height());

        // encoding type
        buffer.put((byte) (fullFrame ? 1 : 0));

        // chunk count
        buffer.put((byte) (snapshot.layerCount() + NON_LAYER_CHUNKS));
    }
}
