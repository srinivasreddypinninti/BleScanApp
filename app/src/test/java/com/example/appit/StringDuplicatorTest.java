package com.example.appit;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StringDuplicatorTest {

    StringDuplicator SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new StringDuplicator();
    }

    @Test
    public void duplicate_emptyString_emptyStringReturned() {
        String result = SUT.duplicate("");
        assertEquals("", result);
    }

    @Test
    public void duplicate_singleChar_twoTimeSingleCharReturned() {
        String result = SUT.duplicate("A");
        assertEquals("AA", result);
    }

    @Test
    public void duplicate_longString_twoTimesLongStringReturned() {
        String result = SUT.duplicate("srinivas reddy");
        assertEquals("srinivas reddysrinivas reddy", result);
    }
}