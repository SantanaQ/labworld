package com.sim.layer;

import com.sim.layer.step.LayerStep;
import com.sim.layer.time_behavior.TimeBehavior;
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

    int width();
    int height();

    default <T> T capability (Class<T> type) {
        if(type.isInstance(this)) {
            return type.cast(this);
        }
        return null;
    }


    default void updatePotential(float time) {
        float[][] potential = potential();
        int h = height();
        int w = width();

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                float val = timeBehavior().sample(source(), x, y, time);
                for (LayerStep step : compositingSteps()) {
                    val = step.apply(val, x, y);
                }
                potential[y][x] = val;
            }
        }
    }

    default void printValues() {
        for(int y = 0; y < height(); y++) {
            for(int x = 0; x < width(); x++) {
                System.out.print(accessibleAt(x, y) + " ");
            }
            System.out.println();
        }
    }

    default float accessibleAtSafe(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height()) {
            return 0;
        }
        return accessibleAt(x, y);
    }

}
