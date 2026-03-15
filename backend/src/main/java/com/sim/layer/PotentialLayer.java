package com.sim.layer;

import com.sim.layer.step.LayerStep;
import com.sim.layer.time_behavior.TimeBehavior;
import com.sim.layer.update.PotentialUpdater;
import com.sim.signal.SignalSource;

import java.util.List;

public class PotentialLayer implements WorldLayer{

    final float[][] potential;

    private final SignalSource signal;
    private final TimeBehavior timeBehavior;
    private final List<LayerStep> compositing;
    private final PotentialUpdater potentialUpdater;

    public PotentialLayer(int width, int height, SignalSource signal,
                          TimeBehavior timeBehavior, List<LayerStep> compositing, PotentialUpdater updater) {
        this.potential = new float[height][width];

        this.signal = signal;
        this.timeBehavior = timeBehavior;
        this.compositing = compositing;
        this.potentialUpdater = updater;
        this.updatePotential(1);
    }

    public final float applyTime(float x, float y, float time) {
        return timeBehavior.sample(signal, x, y, time);
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
        return potential[0].length;
    }

    public final int height() {
        return potential.length;
    }

    @Override
    public float valueAt(int x, int y) {
        if(isInBounds(x, y)) {
            return potential[y][x];
        }
        return 0;
    }

    @Override
    public float[][] values() {
        return potential;
    }

    public final boolean isInBounds(int x, int y) {
        return x >= 0 && x < width() && y >= 0 && y < height();
    }


}
