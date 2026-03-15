package com.sim.layer;

import com.sim.layer.step.LayerStep;
import com.sim.layer.time_behavior.Fixed;
import com.sim.layer.time_behavior.TimeBehavior;
import com.sim.layer.update.DefaultPotentialUpdater;
import com.sim.layer.update.InactiveStateUpdater;
import com.sim.layer.update.PotentialUpdater;
import com.sim.layer.update.StateUpdater;
import com.sim.signal.GridSignal;
import com.sim.signal.SignalField;
import com.sim.signal.SignalSource;

import java.util.ArrayList;
import java.util.List;

public class LayerBuilder {

    private int width;
    private int height;

    private SignalField signalField;

    //private SignalSource signal;
    private TimeBehavior timeBehavior;
    private List<LayerStep> compositing;

    private PotentialUpdater potentialUpdater;
    private StateUpdater stateUpdater;

    public LayerBuilder(int width, int height) {
        this.width = width;
        this.height = height;
        initDefaults();
    }

    private void initDefaults() {
        this.compositing = new ArrayList<>();
        this.timeBehavior = new Fixed();
        SignalSource signal = new GridSignal(new float[height][width]);
        this.signalField = new SignalField(width, height, signal);
        this.potentialUpdater = new DefaultPotentialUpdater();
        this.stateUpdater = new InactiveStateUpdater();
    }

    public LayerBuilder withTimeBehavior(TimeBehavior timeBehavior) {
        if(timeBehavior != null) {
            this.timeBehavior = timeBehavior;
        }
        return this;
    }

    public LayerBuilder withSignal(SignalSource signal) {
        if(signal != null) {
            //this.signal = signal;
            this.signalField = new SignalField(width, height, signal);
        }
        return this;
    }

    public LayerBuilder withCompositing(List<LayerStep> compositing) {
        if (compositing != null) {
            this.compositing.addAll(compositing);
        }
        return this;
    }

    public LayerBuilder withCompositingStep(LayerStep step) {
        if(step != null) {
            this.compositing.add(step);
        }
        return this;
    }

    public LayerBuilder withPotentialUpdater(PotentialUpdater potentialUpdater) {
        if(potentialUpdater != null) {
            this.potentialUpdater = potentialUpdater;
        }
        return this;
    }

    public LayerBuilder withStateUpdater(StateUpdater stateUpdater) {
        if(stateUpdater != null) {
            this.stateUpdater = stateUpdater;
        }
        return this;
    }

    public PotentialLayer buildPotentialLayer() {
        PotentialLayer layer = new PotentialLayer(width, height, signalField,
                timeBehavior, compositing, potentialUpdater);
        return layer;
    }

    public StateLayer buildStateLayer() {
        StateLayer layer = new StateLayer(width, height,
                signalField, timeBehavior, compositing,
                potentialUpdater, stateUpdater);
        return layer;
    }








}
