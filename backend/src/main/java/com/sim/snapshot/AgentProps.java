package com.sim.snapshot;

public enum AgentProps {
    ID(Short.BYTES),
    X(Float.BYTES),
    Y(Float.BYTES),
    VX(Float.BYTES),
    VY(Float.BYTES),
    SPEED(Byte.BYTES),;

    private final int byteSize;

    AgentProps(int byteSize) {
        this.byteSize = byteSize;
    }

    public static int totalBytes() {
        int size = 0;
        for(AgentProps props : AgentProps.values()) {
            size += props.byteSize;
        }
        return size;
    }

}
