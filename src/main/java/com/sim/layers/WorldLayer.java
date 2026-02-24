package com.sim.layers;

import com.sim.layers.step.LayerStep;
import com.sim.layers.time_behavior.TimeBehavior;
import com.sim.noise.SignalSource;
import com.sim.world.Coordinate;

import java.util.List;

public interface WorldLayer {
    float[][] potential();
    SignalSource source();
    TimeBehavior timeBehavior();
    List<LayerStep> compositingSteps();
    float potentialAt(Coordinate coord);
    float accessableAt(Coordinate coord);


    default void updatePotential(float time) {
        float[][] potential = potential();
        int w = potential.length;
        int h = potential[0].length;

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                Coordinate c = new Coordinate(x, y);
                float val = timeBehavior().sample(source(), x, y, time);
                for (LayerStep step : compositingSteps()) {
                    val = step.apply(val, c);
                }
                potential[x][y] = val;
            }
        }
    }
}
