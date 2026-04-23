package com.sim.world.layer.step;

import com.sim.layer.step.Clamp;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClampTest {

    @Test
    void smaller_value_is_clamped_to_min_value() {
        float min = 0.2f;
        float max = 0.4f;
        Clamp clamp = new Clamp(min, max);
        float sample = 0.1f;
        float val = clamp.apply(sample, 0, 0);
        assertEquals(min, val);
    }

    @Test
    void greater_value_is_clamped_to_max_value() {
        float min = 0.2f;
        float max = 0.4f;
        Clamp clamp = new Clamp(min, max);
        float sample = 0.5f;
        float val = clamp.apply(sample, 0, 0);
        assertEquals(max, val);
    }

    @Test
    void value_in_bounds_does_not_get_clamped_to_border_vals() {
        float min = 0.2f;
        float max = 0.4f;
        Clamp clamp = new Clamp(min, max);
        float sample = 0.200002f;
        float val = clamp.apply(sample, 0, 0);
        assertEquals(0.200002f, val);
    }

}
