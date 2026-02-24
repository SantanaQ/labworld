package com.sim.layers.step;

import com.sim.world.Coordinate;

public interface LayerStep {

    float apply(float value, Coordinate coordinate);

    default float smoothstep(float edge0, float edge1, float v) {
        float t = Math.clamp((v - edge0) / (edge1 - edge0), 0f, 1f);
        return t * t * (3f - 2f * t);
    }

}
