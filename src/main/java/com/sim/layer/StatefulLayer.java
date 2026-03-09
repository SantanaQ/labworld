package com.sim.layer;

import com.sim.world.Coordinate;

public interface StatefulLayer extends WorldLayer {
    float[][] state();
    float[][] agentFields();
    void updateState();
    float stateAt(Coordinate coord);
}
