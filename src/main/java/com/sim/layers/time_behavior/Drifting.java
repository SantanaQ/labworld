package com.sim.layers.time_behavior;

import com.sim.noise.SignalSource;

public class Drifting implements TimeBehavior {

    private final float angle; //radian
    private final float speed;

    private boolean active = true;

    public Drifting(float speed, float angle) {
        this.angle = angle;
        this.speed = speed;
    }

    @Override
    public float sample(SignalSource source, float x, float y, float time) {
        if(!active) {
            return source.sample(x,y);
        }
        float dirX = (float) Math.cos(angle);
        float dirY = (float) Math.sin(angle);

        return source.sample(
                x + dirX * speed * time,
                y + dirY * speed * time
        );
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

}
