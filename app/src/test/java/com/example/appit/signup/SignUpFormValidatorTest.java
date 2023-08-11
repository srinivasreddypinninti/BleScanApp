package com.example.appit.signup;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SignUpFormValidatorTest {

    SignUpFormValidator SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new SignUpFormValidator();
    }

    @Test
    public void singUpFormValidator_passingValidFirstName_trueReturned() {
        // Arrange
//        SUT = new SignUpFormValidator();

        // Act
        boolean result = SUT.isFirstNameValid("srinivas");

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void singUpFormValidator_passingShortFirstName_falseReturned() {
        // Arrange
//        SUT = new SignUpFormValidator();

        // Act
        boolean result = SUT.isFirstNameValid("S");

        // Assert
        Assert.assertFalse("returned FALSE, expected to return TRUE", result);
    }

    @Test
    public void name() {
        // Arrange
        // Act
        // Assert
    }

    @After
    public void tearDown() throws Exception {
        SUT = null;
    }
}