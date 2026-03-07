package com.sim.layers;

import com.sim.layers.step.LayerReferenceStep;
import com.sim.layers.step.LayerStep;
import com.sim.layers.time_behavior.TimeBehavior;
import com.sim.signal.SignalSource;
import com.sim.world.Coordinate;

import java.util.List;

public class ProceduralLayer implements WorldLayer, Renderable {

    private final SignalSource source;
    private final List<LayerStep> compositingSteps;
    private final TimeBehavior timeBehavior;

    private final float[][] potential;

    public ProceduralLayer(SignalSource source,
                           TimeBehavior timeBehavior,
                           List<LayerStep> compositingSteps,
                           float[][] potential) {
        this.source = source;
        this.compositingSteps = compositingSteps;
        this.timeBehavior = timeBehavior;
        this.potential = potential;
    }

    @Override
    public float[][] renderValues() {
        return potential;
    }

    @Override
    public float[][] potential() {
        return potential;
    }

    @Override
    public SignalSource source() {
        return source;
    }

    @Override
    public TimeBehavior timeBehavior() {
        return timeBehavior;
    }

    @Override
    public List<LayerStep> compositingSteps() {
        return compositingSteps;
    }

    @Override
    public float potentialAt(Coordinate coord) {
        return potential[coord.y()][coord.x()];
    }

    @Override
    public float accessibleAt(int x, int y) {
        return potential[y][x];
    }

}
