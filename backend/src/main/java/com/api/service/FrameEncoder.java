package com.api.service;

import com.api.service.frame_layouts.WorldLayout;
import com.sim.snapshot.WorldSnapshot;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class FrameEncoder {

    private final ByteBuffer buffer;
    private final FloatBuffer floats;

    public FrameEncoder(WorldLayout layout) {
        buffer = ByteBuffer
                .allocateDirect(layout.totalSize)
                .order(ByteOrder.LITTLE_ENDIAN);

        floats = buffer.asFloatBuffer();
    }

    public ByteBuffer encode(WorldSnapshot snap) {

        buffer.clear();
        floats.clear();

        // Header
        buffer.putInt(snap.worldId());
        buffer.putInt(snap.width());
        buffer.putInt(snap.height());

        floats.position(buffer.position() / Float.BYTES);

        // Layers
        floats.put(snap.heat());
        floats.put(snap.food());
        floats.put(snap.scent());

        // Agents
        floats.put(snap.agents());

        buffer.position(floats.position() * Float.BYTES);

        buffer.flip();

        return buffer;
    }
}
