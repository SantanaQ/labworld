package com.sim.snapshot;

import com.sim.agent.Position;
import com.sim.agent.Velocity;

public class AgentSnapshot {

    private final Position pos;
    private final Velocity velocity;
    private final double speed;

    public AgentSnapshot(Position pos, Velocity velocity, double speed) {
        this.pos = pos;
        this.velocity = velocity;
        this.speed = speed;
    }

    public Position position() {
        return pos;
    }

    public Velocity velocity() {
        return velocity;
    }

    public double speed() {
        return speed;
    }

}
