package com.sim.layers;

import com.sim.layers.step.LayerReferenceStep;
import com.sim.signal.FractalNoise;
import com.sim.signal.SignalSource;
import com.sim.signal.ValueNoise;
import com.sim.layers.step.LayerStep;
import com.sim.layers.time_behavior.Fixed;
import com.sim.layers.time_behavior.TimeBehavior;

import java.util.ArrayList;
import java.util.List;

public class LayerBuilder {

    private final float[][] potential;

    private SignalSource source = new FractalNoise(
            new ValueNoise(123, 16),
            4,
            2);

    private TimeBehavior timeBehavior = new Fixed();

    private final List<LayerStep> compositingSteps = new ArrayList<>();
    private final List<LayerReferenceStep> compositingReferences = new ArrayList<>();

    private float relaxation = 0.05f;

    public LayerBuilder(int width, int height) {
        potential = new float[width][height];
    }

    public LayerBuilder withSignalSource(SignalSource source) {
        this.source = source;
        return this;
    }

    public LayerBuilder withTimeBehaviour(TimeBehavior behavior) {
        this.timeBehavior = behavior;
        return this;
    }

    public LayerBuilder step(LayerStep step) {
        this.compositingSteps.add(step);
        if(step instanceof LayerReferenceStep) {
            compositingReferences.add((LayerReferenceStep) step);
        }
        return this;
    }

    public LayerBuilder withSteps(List<LayerStep> steps) {
        for(LayerStep step : steps) {
            step(step);
        }
        return this;
    }

    public LayerBuilder withRelaxation(float value) {
        this.relaxation = value;
        return this;
    }

    public InteractiveLayer buildInteractiveLayer()
    {
        InteractiveLayer layer = new InteractiveLayer(source, timeBehavior,
                compositingSteps , potential, relaxation);
        layer.updatePotential(1);
        layer.updateState();
        return layer;
    }

    public ProceduralLayer buildProceduralLayer()
    {
        ProceduralLayer layer = new ProceduralLayer(source, timeBehavior,
                compositingSteps, potential);
        layer.updatePotential(1);
        return layer;
    }









}
