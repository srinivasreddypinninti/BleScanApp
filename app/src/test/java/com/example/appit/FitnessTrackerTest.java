package com.example.appit;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FitnessTrackerTest {

    FitnessTracker SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new FitnessTracker();
    }

    @Test
    public void step_totalIncremented() {
        SUT.step();
        assertEquals(1, SUT.getTotalSteps());
    }

    @Test
    public void runStep_totalIncremented() {
        SUT.runStep();
        assertEquals(2, SUT.getTotalSteps());
    }
}