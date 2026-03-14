package com.sim.layer.update;

public class InactiveStateUpdater implements StateUpdater{

    @Override
    public void update(float[][] potential, float[][] state,
                       float[][] nextState, float[][] influence) {
        return;
    }
}
