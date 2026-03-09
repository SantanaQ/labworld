package com.sim.layer.step;

import com.sim.world.Coordinate;

public interface LayerStep {

    float apply(float value, int x, int y);

}
