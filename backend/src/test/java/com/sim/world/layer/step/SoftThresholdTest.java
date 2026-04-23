package com.sim.world.layer.step;

import com.sim.layer.step.SoftThreshold;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SoftThresholdTest {

    @Test
    void value_smaller_than_start_threshold_t0_is_being_set_to_zero() {
        SoftThreshold threshold = new SoftThreshold(0.5f, 0.1f);
        float sample = 0.3f;
        float val = threshold.apply(sample, 0,0);
        assertEquals(0f, val);
    }

    @Test
    void value_greater_than_end_threshold_t1_is_not_changed() {
        SoftThreshold threshold = new SoftThreshold(0.5f, 0.1f);
        float sample = 0.6f;
        float val = threshold.apply(sample, 0,1);
        assertEquals(sample, val);
    }

    @Test
    void value_in_bounds_of_start_threshold_t0_and_end_threshold_t1_is_being_reduced_with_factor_of_normalized_scale() {
        SoftThreshold threshold = new SoftThreshold(0.5f, 0.2f);
        float sample = 0.4f;
        float val = threshold.apply(sample, 0,0);
        assertEquals(0.1f, val, 0.001f);
    }



}
