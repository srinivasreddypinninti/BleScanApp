package com.example.appit;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;


import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class StringRetrieverTest {

    public static final int ID = 10;
    public static final String STRING = "string";

    @Mock
    Context mContextMock;

    StringRetriever SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new StringRetriever(mContextMock);
    }

    @Test
    public void getString_correctParameterPassedToContext() throws Exception{
        // Arrange
        // Act
        SUT.getString(ID);
        // Assert
        verify(mContextMock).getString(ID);
    }

    @Test
    public void getString_correctResultReturned() throws Exception {
        // Arrange
            when(mContextMock.getString(ID)).thenReturn(STRING);
        // Act
            String result = SUT.getString(ID);
        // Assert
        assertEquals(STRING, result);
    }
}