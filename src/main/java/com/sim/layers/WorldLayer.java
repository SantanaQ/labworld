package com.sim.layers;

import com.sim.layers.step.LayerStep;
import com.sim.layers.time_behavior.TimeBehavior;
import com.sim.signal.SignalSource;
import com.sim.world.Coordinate;

import java.util.List;

public interface WorldLayer {

    float[][] potential();
    SignalSource source();
    TimeBehavior timeBehavior();
    List<LayerStep> compositingSteps();
    float potentialAt(Coordinate coord);
    float accessibleAt(int x, int y);

    default void updatePotential(float time) {
        float[][] potential = potential();
        int w = potential.length;
        int h = potential[0].length;

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                Coordinate c = new Coordinate(x, y);
                float val = timeBehavior().sample(source(), x, y, time);
                for (LayerStep step : compositingSteps()) {
                    val = step.apply(val, c);
                }
                potential[y][x] = val;
            }
        }
    }

    default void printValues() {
        for(int y = 0; y < potential().length; y++) {
            for(int x = 0; x < potential()[y].length; x++) {
                System.out.print(accessibleAt(x, y) + " ");
            }
            System.out.println();
        }
    }

    default float accessibleAtSafe(int x, int y) {
        if (x < 0 || x >= potential().length
                || y < 0 || y >= potential().length) {
            return 0;
        }
        return accessibleAt(x, y);
    }

}
