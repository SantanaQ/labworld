package com.sim.agent;

public class Vector {

    private float vx;
    private float vy;

    public Vector(float vx, float vy) {
        this.vx = vx;
        this.vy = vy;
    }

    public Vector add(Vector other) {
        this.vx += other.vx;
        this.vy += other.vy;
        return this;
    }

    public Vector multiply(float multiplier) {
        this.vx *= multiplier;
        this.vy *= multiplier;
        return this;
    }

    public Vector normalize() {
        float len = length();
        if(len > 0){
            this.vx /= len;
            this.vy /= len;
        }
        return this;
    }

    public Vector copyOf(Vector other) {
        this.vx = other.vx;
        this.vy = other.vy;
        return this;
    }

    public Vector copy() {
        return new Vector(vx, vy);
    }


    public Vector set(float vx, float vy) {
        this.vx = vx;
        this.vy = vy;
        return this;
    }

    public Vector rotate(float angle) {
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);

        float newX = vx * cos - vy * sin;
        float newY = vx * sin + vy * cos;
        this.vx = newX;
        this.vy = newY;


        return this;
    }

    public Vector scale(float scale) {
        this.vx *= scale;
        this.vy *= scale;
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

    @Override
    public String toString() {
        return "Velocity [vx=" + vx + ", vy=" + vy + "]";
    }

}
