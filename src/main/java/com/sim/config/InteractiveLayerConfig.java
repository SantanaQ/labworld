package com.sim.config;

import com.sim.layers.LayerBuilder;
import com.sim.layers.WorldLayer;
import com.sim.layers.step.Normalize;

public class InteractiveLayerConfig extends LayerConfig {

    public InteractiveLayerConfig(int width, int height, int seed) {
        super(width, height, seed);
    }

    @Override
    public WorldLayer build() {
        return new LayerBuilder(width, height)
                .withSignalSource(signalSource)
                .withTimeBehaviour(timeBehavior)
                .withSteps(compositing)
                .step(new Normalize(0,1))
                .buildInteractiveLayer();
    }
}
