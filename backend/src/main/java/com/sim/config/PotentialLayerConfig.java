package com.sim.config;

import com.sim.layer.WorldLayer;
import com.sim.layer.LayerBuilder;
import com.sim.layer.step.Clamp;
import com.sim.layer.step.Normalize;
import com.sim.layer.update.PotentialUpdater;

public class PotentialLayerConfig extends LayerConfig {

    PotentialUpdater potentialUpdater;

    public PotentialLayerConfig(int width, int height) {
        super(width, height);
    }

    public void setPotentialUpdater(PotentialUpdater potentialUpdater) {
        this.potentialUpdater = potentialUpdater;
    }

    @Override
    public WorldLayer build() {
        return new LayerBuilder(width, height)
                .withSignal(signalSource)
                .withTimeBehavior(timeBehavior)
                .withPotentialUpdater(potentialUpdater)
                .withCompositing(compositing)
                .withCompositingStep(new Normalize(0,1))
                .withCompositingStep(new Clamp(0,1))
                .buildPotentialLayer();
    }
}
