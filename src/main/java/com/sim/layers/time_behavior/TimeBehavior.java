package com.sim.layers.time_behavior;

import com.sim.signal.SignalSource;


public interface TimeBehavior {

    float sample(SignalSource source, float x, float y, float time);

    void setActive(boolean active);
}
