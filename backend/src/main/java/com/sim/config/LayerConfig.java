package com.sim.config;

import com.sim.layer.WorldLayer;
import com.sim.layer.step.LayerStep;
import com.sim.layer.time_behavior.TimeBehavior;
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

    public LayerConfig(int width, int height) {
        this.width = width;
        this.height = height;
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

    public void addCompositing(List<LayerStep> compositing) {
        this.compositing.addAll(compositing);
    }

    public abstract WorldLayer build();

    public void markDirty() { dirty = true; }

    public boolean isDirty() { return dirty; }

    public void clearDirty() { dirty = false; }

    public List<LayerStep> layerSteps() { return compositing; }

    public TimeBehavior timeBehavior() { return timeBehavior; }

    public SignalSource signalSource() { return signalSource; }

}
