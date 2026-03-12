package com.sim.layer.time_behavior;

import com.sim.signal.SignalSource;

public class Fixed implements TimeBehavior {


    @Override
    public float sample(SignalSource source, float x, float y, float time) {
        return source.sample(x, y);
    }

    @Override
    public void setActive(boolean active) {
        // should not affect sampling
    }

}
