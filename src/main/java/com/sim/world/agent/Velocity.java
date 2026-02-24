package com.sim.world.agent;

public record Velocity(float vx, float vy) {

    public Velocity add(Velocity other) {
        float addedX = this.vx() + other.vx();
        float addedY = this.vy() + other.vy();
        return new Velocity(addedX, addedY);
    }

}
