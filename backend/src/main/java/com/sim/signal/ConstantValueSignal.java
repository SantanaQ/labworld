package com.sim.signal;

public class ConstantValueSignal implements SignalSource{

    private final float value;

    public ConstantValueSignal(float value) {
        this.value = value;
    }

    @Override
    public float sample(float x, float y) {
        return value;
    }
}
