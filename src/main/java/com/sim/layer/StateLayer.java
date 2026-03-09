package com.sim.layer;

import com.sim.layer.step.LayerStep;
import com.sim.layer.time_behavior.TimeBehavior;
import com.sim.layer.update.PotentialUpdater;
import com.sim.layer.update.StateUpdater;
import com.sim.signal.SignalSource;

import java.util.Arrays;
import java.util.List;

public class StateLayer extends PotentialLayer {

    private float[][] state;
    private float[][] next;
    private final float[][] influence;

    StateUpdater stateUpdater;

    public StateLayer(int width, int height, SignalSource signal,
                      TimeBehavior timeBehavior, List<LayerStep> compositing,
                      PotentialUpdater potentialUpdater, StateUpdater stateUpdater) {
        super(width, height, signal, timeBehavior, compositing, potentialUpdater);
        this.stateUpdater = stateUpdater;
        this.state = new float[height][width];
        this.next = new float[height][width];
        this.influence = new float[height][width];
    }

    @Override
    public float valueAt(int x, int y) {
        if(isInBounds(x, y)) {
            return state[y][x];
        }
        return 0;
    }

    @Override
    public float[][] values() {
        return state;
    }

    public float stateAt(int x, int y) {
        return state[y][x];
    }

    public float influenceAt(int x, int y) {
        return influence[y][x];
    }

    public void applyInfluence(int x, int y, float value) {
        influence[y][x] += value;
    }

    public void setState(int x, int y, float value) {
        state[y][x] = value;
    }

    public void setNextState(int x, int y, float value) {
        next[y][x] = value;
    }

    public void updateState() {
        stateUpdater.update(this);
    }

    public void swapState() {
        float[][] temp = state;
        state = next;
        next = temp;
    }

    public void clearInfluence() {
        for(float[] row : influence) {
            Arrays.fill(row, 0);
        }
    }


}
