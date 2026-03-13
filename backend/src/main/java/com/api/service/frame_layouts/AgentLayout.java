package com.api.service.frame_layouts;

public class AgentLayout {

    public static final int POSITION_OFFSET = 0;           // vec2
    public static final int VELOCITY_OFFSET = POSITION_OFFSET + 2 * Float.BYTES;
    public static final int NEEDS_OFFSET    = VELOCITY_OFFSET + 2 * Float.BYTES;
    public static final int SPEED_OFFSET    = NEEDS_OFFSET + 4 * Float.BYTES;
    public static final int STRIDE = SPEED_OFFSET + Float.BYTES;

}
