package com.sim.agent;

public class Velocity {

    private float vx;
    private float vy;

    public Velocity(float vx, float vy) {
        this.vx = vx;
        this.vy = vy;
    }

    public Velocity add(Velocity other) {
        this.vx += other.vx;
        this.vy += other.vy;
        return this;
    }

    public Velocity multiply(float multiplier) {
        this.vx *= multiplier;
        this.vy *= multiplier;
        return this;
    }

    public Velocity normalize() {
        float len = length();
        if(len > 0){
            this.vx /= len;
            this.vy /= len;
        }
        return this;
    }



    public float length() {
        return (float) Math.sqrt(this.vx * this.vx + this.vy * this.vy);
    }

    public float vy() {
        return vy;
    }

    public float vx() {
        return vx;
    }

}
