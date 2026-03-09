package com.sim.config;

import com.sim.layer.LayerBuilder;
import com.sim.layer.WorldLayer;
import com.sim.layer.step.Clamp;
import com.sim.layer.step.Normalize;

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
