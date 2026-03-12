package com.sim.snapshot;

import com.sim.agent.Position;
import com.sim.agent.Vector;

public class AgentSnapshot {

    private final Position pos;
    private final Vector velocity;
    private final double speed;

    public AgentSnapshot(Position pos, Vector velocity, double speed) {
        this.pos = pos;
        this.velocity = velocity;
        this.speed = speed;
    }

    public Position position() {
        return pos;
    }

    public Vector velocity() {
        return velocity;
    }

    public double speed() {
        return speed;
    }

}
