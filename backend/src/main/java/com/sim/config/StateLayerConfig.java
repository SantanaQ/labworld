package com.sim.config;

import com.sim.layer.WorldLayer;
import com.sim.layer.LayerBuilder;
import com.sim.layer.step.Clamp;
import com.sim.layer.step.Normalize;
import com.sim.layer.update.StateUpdater;

public class StateLayerConfig extends PotentialLayerConfig {

    private StateUpdater stateUpdater;

    public StateLayerConfig(int width, int height) {
        super(width, height);
    }

    public void setStateUpdater(StateUpdater stateUpdater) {
        this.stateUpdater = stateUpdater;
    }

    @Override
    public WorldLayer build() {
        return new LayerBuilder(width, height)
                .withSignal(signalSource)
                .withTimeBehavior(timeBehavior)
                .withPotentialUpdater(potentialUpdater)
                .withStateUpdater(stateUpdater)
                .withCompositing(compositing)
                .withCompositingStep(new Clamp(0,1))
                .buildStateLayer();
    }
}
