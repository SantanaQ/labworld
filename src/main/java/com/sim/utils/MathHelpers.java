package com.sim.utils;

public class MathHelpers {

    public static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    public static float smoothstep(float edge0, float edge1, float v) {
        float t = Math.clamp((v - edge0) / (edge1 - edge0), 0f, 1f);
        return t * t * (3f - 2f * t);
    }

}
