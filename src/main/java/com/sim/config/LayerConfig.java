package com.sim.config;

import com.sim.layers.WorldLayer;
import com.sim.layers.step.LayerReferenceStep;
import com.sim.layers.step.LayerStep;
import com.sim.layers.time_behavior.TimeBehavior;
import com.sim.signal.SignalSource;

import java.util.ArrayList;
import java.util.List;

public abstract class LayerConfig {
    private boolean dirty = true;

    SignalSource signalSource;
    TimeBehavior timeBehavior;
    List<LayerStep> compositing;

    int width;
    int height;
    int seed;

    public LayerConfig(int width, int height, int seed) {
        this.width = width;
        this.height = height;
        this.seed = seed;
        this.compositing = new ArrayList<>();
    }

    public void setSignalSource(SignalSource signalSource) {
        this.signalSource = signalSource;
    }

    public void setTimeBehavior(TimeBehavior timeBehavior) {
        this.timeBehavior = timeBehavior;
    }

    public void addLayerStep(LayerStep layerStep) {
        compositing.add(layerStep);
    }

    public abstract WorldLayer build();

    public void markDirty() { dirty = true; }

    public boolean isDirty() { return dirty; }

    public void clearDirty() { dirty = false; }

    public List<LayerStep> layerSteps() { return compositing; }

}
