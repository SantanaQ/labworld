package com.sim.layer.time_behavior;

import com.sim.signal.SignalField;

public class DomainWarp implements TimeBehavior {

    private final SignalField warpX;
    private final SignalField warpY;
    private final float strength;

    public DomainWarp(
            SignalField warpX,
            SignalField warpY,
            float strength) {

        this.warpX = warpX;
        this.warpY = warpY;
        this.strength = strength;
    }

    @Override
    public float sample(SignalField src, int x, int y, float time) {

        int dx = (int)(warpX.sample(x, y) * strength);
        int dy = (int)(warpY.sample(x, y) * strength);

        return src.sample(x + dx, y + dy);
    }

}
