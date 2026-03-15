package com.sim.layer.time_behavior;

import com.sim.signal.SignalField;

public interface TimeBehavior {

    float sample(SignalField source, int x, int y, float time);

}
