package com.sim.layer;

import com.sim.layer.step.LayerStep;
import com.sim.layer.time_behavior.TimeBehavior;
import com.sim.signal.SignalSource;
import com.sim.world.Coordinate;

import java.util.Arrays;
import java.util.List;

public class InteractiveLayer implements StatefulLayer, AgentAffectable, Renderable {

    private final SignalSource source;
    private final List<LayerStep> compositingSteps;
    private final TimeBehavior timeBehavior;

    private final float[][] potential;
    private final float[][] agentFields;
    private float[][] state;
    private float[][] nextState;

    private final float relaxation;


    public InteractiveLayer(SignalSource source,
                            TimeBehavior timeBehavior,
                            List<LayerStep> compositingSteps,
                            float[][] potential,
                            float relaxation) {
        this.source = source;
        this.compositingSteps = compositingSteps;
        this.timeBehavior = timeBehavior;
        this.potential = potential;
        this.relaxation = relaxation;

        this.state = new float[potential.length][potential[0].length];
        this.nextState = new float[potential.length][potential[0].length];
        this.agentFields = new float[potential.length][potential[0].length];

        for (int x = 0; x < state[0].length; x++) {
            state[x] = Arrays.copyOf(potential[x], state.length);
        }
    }

    @Override
    public void applyAgentInfluence(Coordinate c, float value) {
        agentFields[c.y()][c.x()] += value;
    }

    @Override
    public float[][] renderValues() {
        return state;
    }

    @Override
    public float[][] state() {
        return state;
    }

    @Override
    public float[][] agentFields() {
        return agentFields;
    }

    public void updateState() {
        int w = state[0].length;
        int h = state.length;

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {

                float s = state[y][x];
                float p = potential[y][x];

                float laplace = accessibleAtSafe(x-1, y-1)   * 0.05f +
                                accessibleAtSafe(x, y-1)     * 0.20f +
                                accessibleAtSafe(x+1, y-1)   * 0.05f +
                                accessibleAtSafe(x-1, y)     * 0.20f +
                                accessibleAtSafe(x+1, y)     * 0.20f +
                                accessibleAtSafe(x-1, y+1)   * 0.05f +
                                accessibleAtSafe(x, y+1)     * 0.20f +
                                accessibleAtSafe(x+1, y+1)   * 0.05f;


                // diffusion
                s += 0.2f * (laplace - s);

                // relaxation towards procedural potential
                s += relaxation * (p - s);

                // agent influence
                s += agentFields[y][x];
                s = Math.clamp(s, 0f, 1f);

                // decay
                s *= 0.995f;

                nextState[y][x] = s;
            }
        }
        clearAgentFields();
        float[][] tmp = state;
        state = nextState;
        nextState = tmp;
    }

    @Override
    public float stateAt(Coordinate coord) {
        return state[coord.y()][coord.x()];
    }

    private void clearAgentFields() {
        for(float[] row : agentFields) {
            Arrays.fill(row, 0);
        };
    }

    @Override
    public float[][] potential() {
        return potential;
    }

    @Override
    public SignalSource source() {
        return source;
    }

    @Override
    public TimeBehavior timeBehavior() {
        return timeBehavior;
    }

    @Override
    public List<LayerStep> compositingSteps() {
        return compositingSteps;
    }

    @Override
    public float potentialAt(Coordinate coord) {
        return potential[coord.y()][coord.x()];
    }

    @Override
    public float accessibleAt(int x, int y) {
        return state[y][x];
    }

    @Override
    public int width() {
        return potential[0].length;
    }

    @Override
    public int height() {
        return potential.length;
    }

}
