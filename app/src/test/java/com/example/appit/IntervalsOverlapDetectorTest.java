package com.example.appit;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class IntervalsOverlapDetectorTest {

    IntervalsOverlapDetector SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new IntervalsOverlapDetector();
    }

    // i1 before i2
    @Test
    public void isOverlap_interval1beforeInterval2_falseReturned() {
        Interval i1 = new Interval(-1, 5);
        Interval i2 = new Interval(8, 12);
        boolean result = SUT.isOverlap(i1, i2);
        assertEquals(false, result);
    }

    // i1 overlaps i2 on start
    @Test
    public void isOverlap_interval1OverlapsInterval2OnStart_trueReturned() {
        Interval i1 = new Interval(-1, 5);
        Interval i2 = new Interval(3, 12);
        boolean result = SUT.isOverlap(i1, i2);
        assertEquals(true, result);
    }

    // i1 within i2
    @Test
    public void isOverlap_interval1WithinInterval2_trueReturned() {
        Interval i1 = new Interval(-1, 5);
        Interval i2 = new Interval(0, 4);
        boolean result = SUT.isOverlap(i1, i2);
        assertEquals(true, result);
    }

    // i1 overlaps i2 on end
    @Test
    public void isOverlap_interval1OverlapsInterval2OnEnd_trueReturned() {
        Interval i1 = new Interval(-1, 5);
        Interval i2 = new Interval(-4, 4);
        boolean result = SUT.isOverlap(i1, i2);
        assertEquals(true, result);
    }

    // i1 after i2
    @Test
    public void isOverlap_interval1AfterInterval2OnEnd_falseReturned() {
        Interval i1 = new Interval(5, 12);
        Interval i2 = new Interval(-4, 4);
        boolean result = SUT.isOverlap(i1, i2);
        assertEquals(false, result);
    }
}