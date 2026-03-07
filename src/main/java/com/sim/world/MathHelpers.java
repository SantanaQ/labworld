package com.sim.world;

public class MathHelpers {

    public static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

}
