package com.sim.layer.time_behavior;

import com.sim.signal.SignalField;

public class Drift implements TimeBehavior {

    private final float speed;
    private final float dirX;
    private final float dirY;

    public Drift(float speed, float angle) {
        this.speed = speed;
        this.dirX = (float) Math.cos(angle);
        this.dirY = (float) Math.sin(angle);
    }

    @Override
    public float sample(SignalField source, int x, int y, float time) {
        float offsetX = dirX * speed * time;
        float offsetY = dirY * speed * time;

        int nx = (int)(x + offsetX);
        int ny = (int)(y + offsetY);

        return source.sample(nx, ny);
    }
}
