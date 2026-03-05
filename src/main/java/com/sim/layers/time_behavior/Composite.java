package com.sim.layers.time_behavior;

import com.sim.signal.SignalSource;

import java.util.List;

public final class Composite implements TimeBehavior {

    private final List<TimeBehavior> behaviors;

    public Composite(List<TimeBehavior> behaviors) {
        this.behaviors = behaviors;
    }


    @Override
    public float sample(SignalSource source, float x, float y, float time) {
        SignalSource current = source;

        for (TimeBehavior b : behaviors) {
            SignalSource prev = current;
            current = (px, py) -> b.sample(prev, px, py, time);
        }

        return current.sample(x, y);
    }

    @Override
    public void setActive(boolean active) {
        for (TimeBehavior b : behaviors) {
            b.setActive(active);
        }
    }
}
