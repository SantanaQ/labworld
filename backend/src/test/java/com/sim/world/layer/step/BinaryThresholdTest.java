package com.sim.world.layer.step;

import com.sim.layer.step.BinaryThreshold;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class BinaryThresholdTest {

    @Test
    void if_value_is_less_than_threshold_it_becomes_zero() {
        BinaryThreshold threshold = new BinaryThreshold(0.5f);
        float less = 0.2f;
        float val = threshold.apply(less, 0,0);
        assertEquals(0, val);
    }

    @Test
    void if_value_is_greater_than_threshold_it_becomes_one() {
        BinaryThreshold threshold = new BinaryThreshold(0.5f);
        float greater = 0.7f;
        float val = threshold.apply(greater, 0,0);
        assertEquals(1, val);
    }

    @Test
    void if_value_equals_threshold_it_becomes_one() {
        float equals = 0.5f;
        BinaryThreshold threshold = new BinaryThreshold(equals);
        float val = threshold.apply(equals, 0,0);
        assertEquals(1, val);
    }



}
