package com.sim.layers.time_behavior;

import com.sim.noise.SignalSource;


public interface TimeBehavior {

    float sample(SignalSource source, float x, float y, float time);

}
