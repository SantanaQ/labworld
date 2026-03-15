package com.sim.layer.time_behavior;

import com.sim.signal.SignalField;
import java.util.List;

public final class Composite implements TimeBehavior {

    private final List<TimeBehavior> behaviors;

    public Composite(List<TimeBehavior> behaviors) {
        this.behaviors = behaviors;
    }

    @Override
    public float sample(SignalField source, int x, int y, float time) {

        float value = 0;

        for (TimeBehavior b : behaviors) {
            value += b.sample(source, x, y, time);
        }

        return value;
    }

}
