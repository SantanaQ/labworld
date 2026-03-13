package com.api.service;

public class FrameLayout {

    public final int headerSize;
    public final int layerSize;
    public final int agentSize;
    public final int totalSize;

    public FrameLayout(int layers, int width, int height, int agents, int agentStride) {

        headerSize = 4; // frame id

        layerSize = layers * width * height;

        agentSize = agents * agentStride;

        totalSize = headerSize + layerSize + agentSize;
    }

}
