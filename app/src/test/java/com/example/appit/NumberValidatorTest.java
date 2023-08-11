package com.example.appit;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class NumberValidatorTest {

    NumberValidator SUT;

    @Before
    public void setUp() {
        SUT = new NumberValidator();
    }

    @Test
    public void test1() {
        boolean res = SUT.isPositive(-1);
        Assert.assertFalse(res);
    }

    @Test
    public void test2() {
        boolean res = SUT.isPositive(0);
        Assert.assertFalse(res);
    }

    @Test
    public void test3() {
        boolean res = SUT.isPositive(1);
        Assert.assertTrue(res);
    }

    @After
    public void tearDown() throws Exception {
        SUT = null;
    }


}