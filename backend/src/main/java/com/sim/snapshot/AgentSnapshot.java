package com.sim.snapshot;

import com.sim.agent.Needs;
import com.sim.agent.Position;
import com.sim.agent.Vector;

public class AgentSnapshot {

    private enum NeedIdx {
        HUNGER,
        HEAT,
        CURIOSITY,
        FEAR
    }

    private final Position pos;
    private final Vector velocity;
    private final float speed;
    private final float[] needs;

    public AgentSnapshot(Position pos, Vector velocity, float speed, Needs needs) {
        this.pos = pos;
        this.velocity = velocity;
        this.speed = speed;
        this.needs = new float[NeedIdx.values().length];
        this.needs[NeedIdx.HUNGER.ordinal()] = needs.hunger();
        this.needs[NeedIdx.HEAT.ordinal()] = needs.heat();
        this.needs[NeedIdx.CURIOSITY.ordinal()] = needs.curiosity();
        this.needs[NeedIdx.FEAR.ordinal()] = needs.fear();
    }

    public Position position() {
        return pos;
    }

    public Vector velocity() {
        return velocity;
    }

    public float speed() {
        return speed;
    }

    public float[] needs() {
        return needs;
    }


}
