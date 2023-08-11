package com.example.appit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NegativeNumberValidatorTest {

    NegativeNumberValidator SUT;

    @Before
    public void setUp()  {
        SUT = new NegativeNumberValidator();
    }

    @Test
    public void test1() {
        //Arrange
        //Act
        boolean result = SUT.isNegativeNumber(1);
        //Assert
        assertFalse(result);
    }

    @Test
    public void test2() {
        // Arrange
        //Act
        boolean result = SUT.isNegativeNumber(0);
        // Assert
        assertFalse(result);
    }

    @Test
    public void test3() {
        // Arrange
        // Act
        boolean result = SUT.isNegativeNumber(-1);
        // Assert
        assertTrue(result);
    }



    @After
    public void tearDown()  {
        SUT = null;
    }
}