package com.sim.layers;

import com.sim.world.Coordinate;

public interface AgentAffectable {
    void applyAgentInfluence(Coordinate c, float value);
}
