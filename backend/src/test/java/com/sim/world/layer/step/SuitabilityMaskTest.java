package com.sim.world.layer.step;

import com.sim.layer.LayerContext;
import com.sim.layer.LayerID;
import com.sim.layer.PotentialLayer;
import com.sim.layer.WorldLayer;
import com.sim.layer.step.SuitabilityMask;
import com.sim.layer.time_behavior.Fixed;
import com.sim.layer.update.DefaultPotentialUpdater;
import com.sim.signal.GridSignal;
import com.sim.signal.SignalField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SuitabilityMaskTest {

    SuitabilityMask suitabilityMask;

    @BeforeEach
    public void setUp() {

        int gridSize = 5;

        float[][] grid = new float[][] {
                {0, 0.4f, 0.55f, 0.7f, 1},
                {0, 0.45f, 0.6f, 0.65f, 1},
                {0, 0.4f, 0.55f, 0.7f, 1},
                {0, 0.4f, 0.55f, 0.7f, 1},
                {0, 0.4f, 0.55f, 0.7f, 1},
        };
        GridSignal signal = new GridSignal(grid);
        SignalField field = new SignalField(gridSize, gridSize, signal);

        WorldLayer heat = new PotentialLayer(
                gridSize,
                gridSize,
                field,
                new Fixed(),
                List.of(),
                new DefaultPotentialUpdater());

        LayerContext ctx = new LayerContext();
        ctx.register(LayerID.HEAT, heat);

        suitabilityMask = new SuitabilityMask(LayerID.HEAT, 0.4f, 0.7f);
        suitabilityMask.resolve(ctx);
    }

    @Test
    void when_ref_value_is_smaller_than_min_value_is_set_to_zero() {
        float sample = 1;
        float val = suitabilityMask.apply(sample, 0, 0);
        assertEquals(0, val);
    }

    @Test
    void when_ref_value_equals_to_min_then_value_is_set_to_zero() {
        float sample = 1;
        float val = suitabilityMask.apply(sample, 1, 0);
        assertEquals(0, val);
    }

    @Test
    void when_ref_value_is_near_to_min_value_gets_reduced_strongly() {
        float sample = 1;
        float val = suitabilityMask.apply(sample, 1, 1);
        assertEquals(0.25f, val, 0.01f);
    }

    @Test
    void when_ref_value_is_near_to_max_value_gets_reduced_strongly() {
        float sample = 1;
        float val = suitabilityMask.apply(sample, 3, 1);
        assertEquals(0.25f, val, 0.01f);
    }

    @Test
    void when_ref_value_equals_mid_of_range_between_min_and_max_value_stays_same() {
        float sample = 1;
        float val = suitabilityMask.apply(sample, 2, 0);
        assertEquals(sample, val);
    }

    @Test
    void when_ref_value_equals_to_max_then_value_is_set_to_zero() {
        float sample = 1;
        float val = suitabilityMask.apply(sample, 3, 0);
        assertEquals(0, val);
    }

    @Test
    void when_ref_value_is_greater_than_max_value_is_set_to_zero() {
        float sample = 1;
        float val = suitabilityMask.apply(sample, 4, 0);
        assertEquals(0, val);
    }

}
