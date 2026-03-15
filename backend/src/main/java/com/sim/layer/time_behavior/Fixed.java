package com.sim.layer.time_behavior;

import com.sim.signal.SignalField;
public class Fixed implements TimeBehavior {


    @Override
    public float sample(SignalField source, int x, int y, float time) {
        return source.sample(x, y);
    }

}
