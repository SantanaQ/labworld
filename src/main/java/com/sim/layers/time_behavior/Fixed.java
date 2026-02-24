package com.sim.layers.time_behavior;

import com.sim.noise.SignalSource;

public class Fixed implements TimeBehavior {


    @Override
    public float sample(SignalSource source, float x, float y, float time) {
        return source.sample(x, y);
    }

}
