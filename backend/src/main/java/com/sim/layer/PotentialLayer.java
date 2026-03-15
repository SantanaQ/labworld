package com.sim.layer;

import com.sim.layer.step.LayerStep;
import com.sim.layer.time_behavior.TimeBehavior;
import com.sim.layer.update.PotentialUpdater;
import com.sim.signal.SignalField;

import java.util.List;

public class PotentialLayer implements WorldLayer{


    public final int width;
    public final int height;
    public final float[] potential;


    //private final SignalSource signal;
    private final SignalField signalField;
    private final TimeBehavior timeBehavior;
    private final List<LayerStep> compositing;
    private final PotentialUpdater potentialUpdater;

    public PotentialLayer(int width, int height, SignalField signalField,
                          TimeBehavior timeBehavior, List<LayerStep> compositing, PotentialUpdater updater) {
        this.width = width;
        this.height = height;
        this.potential = new float[width * height];

        this.signalField = signalField;
        this.timeBehavior = timeBehavior;
        this.compositing = compositing;
        this.potentialUpdater = updater;
        this.updatePotential(1);
    }

    public final float applyTime(int x, int y, float time) {
        return timeBehavior.sample(signalField, x, y, time);
    }

    public final float applyCompositing(float originVal, int x, int y) {
        float val = originVal;
        for(LayerStep step : compositing) {
            val = step.apply(val, x, y);
        }
        return val;
    }

    public final void updatePotential(float time) {
        potentialUpdater.update(this, potential, time);
    }

    public final int width() {
        return width;
    }

    public final int height() {
        return height;
    }

    @Override
    public float valueAt(int x, int y) {
        if(isInBounds(x, y)) {
            return potential[y * width + x];
        }
        return 0;
    }

    @Override
    public float[] values() {
        return potential;
    }

    public final boolean isInBounds(int x, int y) {
        return x >= 0 && x < width() && y >= 0 && y < height();
    }


}
