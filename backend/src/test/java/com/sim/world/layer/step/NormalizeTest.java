package com.sim.world.layer.step;

import com.sim.layer.step.Normalize;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NormalizeTest {

    @Test
    void smaller_value_than_min_returns_negative_rescaled_value_in_proportion_of_range() {
        Normalize normalize = new Normalize(0.5f, 0.9f);
        float sample = 0.4f;
        float val = normalize.apply(sample, 0, 0);
        assertEquals(-0.25f, val, 0.01f);
    }

    @Test
    void greater_value_than_max_returns_rescaled_value_greater_than_one_in_proportion_of_range() {
        Normalize normalize = new Normalize(0.5f, 0.9f);
        float sample = 1;
        float val = normalize.apply(sample, 0, 0);
        assertEquals(1.25f, val, 0.01f);
    }

    @Test
    void value_in_bounds_returns_rescaled_value_between_zero_and_one_in_proportion_of_range() {
        Normalize normalize = new Normalize(0.5f, 0.9f);
        float sample = 0.6f;
        float val = normalize.apply(sample, 0, 0);
        assertEquals(0.25f, val, 0.01f);
    }

}
