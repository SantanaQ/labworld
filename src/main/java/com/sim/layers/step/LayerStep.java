package com.sim.layers.step;

import com.sim.world.Coordinate;

public interface LayerStep {

    float apply(float value, Coordinate coordinate);

}
