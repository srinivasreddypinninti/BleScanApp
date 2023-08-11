package com.example.appit.mockito;

import android.content.Context;
import android.content.res.AssetManager;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.stream.IntStream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ContactPresenterTest {

    ContactPresenter SUT;

    @Mock
    Context contextMock;

    @Mock
    AssetManager assetManager;

    @Mock
    InputStream inputStream;

    @Before
    public void setUp() throws Exception {
        SUT = new ContactPresenter(contextMock);
    }

    @Test
    public void loadJson() throws IOException {
        // Arrange
        when(contextMock.getAssets()).thenReturn(assetManager);
        doReturn(inputStream).when(assetManager).open(anyString());
        // Act
        String json = SUT.loadJson();
        // Assert
        Assert.assertNotNull(json);
    }
}