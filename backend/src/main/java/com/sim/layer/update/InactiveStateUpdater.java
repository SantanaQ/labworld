package com.sim.layer.update;

import com.sim.layer.StateLayer;

public class InactiveStateUpdater implements StateUpdater{

    @Override
    public void update(StateLayer layer, float[] potential, float[] state,
                       float[] nextState, float[] influence) {
        return;
    }
}
