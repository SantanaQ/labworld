package com.sim.layers.time_behavior;

import com.sim.noise.SignalSource;

public class Drifting implements TimeBehavior {

    private final float speed;

    public Drifting(float speed) {
        this.speed = speed;
    }

    @Override
    public float sample(SignalSource source, float x, float y, float time) {
        return source.sample(x + time * speed, y + time * speed);
    }

}
