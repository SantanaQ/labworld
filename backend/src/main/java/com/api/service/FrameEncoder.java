package com.api.service;

import com.sim.snapshot.WorldSnapshot;

public class FrameEncoder {

    private byte[] frame;

    public FrameEncoder(FrameLayout layout) {
        frame = new byte[layout.totalSize];
    }

    public byte[] encode(WorldSnapshot snap) {

        int offset = 0;

        frame[offset++] = (byte) snap.worldId();

        offset = encodeLayers(snap, offset);

        offset = encodeAgents(snap, offset);

        return frame;
    }

    private int encodeLayers(WorldSnapshot snap, int offset) {
        return 0;
    }

    private int encodeAgents(WorldSnapshot snap, int offset) {
        return 0;
    }

    public byte[] currentFrame() {
        return frame;
    }

}
