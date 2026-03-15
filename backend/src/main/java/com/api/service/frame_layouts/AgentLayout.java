package com.api.service.frame_layouts;

import com.sim.snapshot.AgentProps;

public class AgentLayout {

    public static final int POSITION_OFFSET = 0; // X,Y
    public static final int VELOCITY_OFFSET = POSITION_OFFSET + 2 * Float.BYTES; // VX, VY
    public static final int SPEED_OFFSET = VELOCITY_OFFSET + 2 * Float.BYTES; // SPEED
    public static final int NEEDS_OFFSET = SPEED_OFFSET + 2 * Float.BYTES; // ENERGY, HUNGER, HEAT, CURIOSITY, FEAR

    public static final int STRIDE = AgentProps.values().length * Float.BYTES;

}
