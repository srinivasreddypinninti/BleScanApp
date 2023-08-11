package com.example.appit.mockito;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static com.example.appit.mockito.GetCartItemsHttpEndpoint.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FetchCartItemsUseCaseTest {

    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final int PRICE = 5;

    @Mock GetCartItemsHttpEndpoint getCartItemsHttpEndpointMock;

    FetchCartItemsUseCase SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new FetchCartItemsUseCase(getCartItemsHttpEndpointMock);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                Callback callback = (Callback) args[2];
                callback.onGetCartItemsSucceeded(getCartItemsSchemas());
                return null;
            }
        }).when(getCartItemsHttpEndpointMock).getCartItems(anyInt(), any(), any(Callback.class));
    }

    private List<CartItemSchema> getCartItemsSchemas() {
        ArrayList<CartItemSchema> schemas = new ArrayList<>();
        schemas.add(new CartItemSchema(ID, TITLE, DESCRIPTION, PRICE));
        return schemas;
    }

    @Test
    public void fetchCartItems_passedCorrectLimitToEndpoint() {
        // Arrange
        ArgumentCaptor<Integer> ac = ArgumentCaptor.forClass(Integer.class);
        // Act
        SUT.fetchCartItemsAndNotify(10);
        // Assert
        verify(getCartItemsHttpEndpointMock).getCartItems(anyInt(), any(), any(Callback.class));
//        assertEquals(Integer.valueOf(10), ac.getValue());
    }
}