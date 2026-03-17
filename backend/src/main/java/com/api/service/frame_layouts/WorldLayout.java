package com.api.service.frame_layouts;

public class WorldLayout {

    public final int headerOffset = 0;
    public final int headerSize;

    public final int layerOffset;
    public final int layerSize;

    public final int agentOffset;
    public final int agentSize;

    public final int totalSize;

    public WorldLayout(int layers, int width, int height, int agents, int agentStride) {

        headerSize = (Long.BYTES * 2) + Integer.BYTES + Integer.BYTES;  // worldId - width -height

        layerOffset = headerOffset + headerSize;
        layerSize = layers * width * height * Float.BYTES;

        agentOffset = layerOffset + layerSize;
        agentSize = agents * agentStride;

        totalSize = agentOffset + agentSize;
    }
}
