package com.sim.config;

import com.sim.layers.LayerBuilder;
import com.sim.layers.WorldLayer;
import com.sim.layers.step.Clamp;
import com.sim.layers.step.Normalize;

public class ProceduralLayerConfig extends LayerConfig {

    public ProceduralLayerConfig(int width, int height, int seed) {
        super(width, height, seed);
    }

    @Override
    public WorldLayer build() {
        return new LayerBuilder(width, height)
                .withSignalSource(signalSource)
                .withTimeBehaviour(timeBehavior)
                .withSteps(compositing)
                .step(new Normalize(0,1))
                .step(new Clamp(0, 1))
                .buildProceduralLayer();
    }
}
