package com.example.appit.mockito;

import android.graphics.Paint;

import com.example.appit.AuthTokenCache;
import com.example.appit.EventBusPoster;
import com.example.appit.LoginEvent;
import com.example.appit.LoginHttpEndpointSync;
import com.example.appit.NetworkErrorException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static com.example.appit.mockito.LoginUseCaseSyncMoc.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class LoginUseCaseSyncMocTest {

    public static final String USERNAME = "userName";
    public static final String PASSWORD = "password";
    public static final String AUTHTOKEN = "authToken";

    LoginHttpEndpointSync mLoginHttpEndpointSyncMock;
    AuthTokenCache mAuthTokenCacheMock;
    EventBusPoster mEventBusPosterMock;

    LoginUseCaseSyncMoc SUT;

    @Before
    public void setUp() throws Exception {
        mLoginHttpEndpointSyncMock = mock(LoginHttpEndpointSync.class);
        mAuthTokenCacheMock = mock(AuthTokenCache.class);
        mEventBusPosterMock = mock(EventBusPoster.class);
        SUT = new LoginUseCaseSyncMoc(mLoginHttpEndpointSyncMock, mAuthTokenCacheMock, mEventBusPosterMock);

        when(mLoginHttpEndpointSyncMock.loginSync(anyString(), anyString()))
                .thenReturn(new LoginHttpEndpointSync.EndpointResult(LoginHttpEndpointSync.EndpointResultStatus.SUCCESS, AUTHTOKEN));
    }

    @Test
    public void loginSync_success_userNamePasswordPassedToEndpoint() throws NetworkErrorException {
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        SUT.loginSync(USERNAME, PASSWORD);
        verify(mLoginHttpEndpointSyncMock, times(1)).loginSync(ac.capture(), ac.capture());
        List<String> captures = ac.getAllValues();
        assertEquals(USERNAME, captures.get(0));
        assertEquals(PASSWORD, captures.get(1));
    }

    @Test
    public void loginSync_success_authTokenCached() throws Exception{
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
       SUT.loginSync(USERNAME, PASSWORD);
       verify(mAuthTokenCacheMock).cacheAuthToken(ac.capture());
        assertEquals(AUTHTOKEN, ac.getValue());
    }

    @Test
    public void loginSync_generalError_authTokenNotCached() throws Exception{
        generalError();
        SUT.loginSync(USERNAME, PASSWORD);
        verifyNoMoreInteractions(mAuthTokenCacheMock);
    }

    public void generalError() throws Exception{
        when(mLoginHttpEndpointSyncMock.loginSync(anyString(), anyString()))
                .thenReturn(new LoginHttpEndpointSync.EndpointResult(LoginHttpEndpointSync.EndpointResultStatus.GENERAL_ERROR, ""));
    }

    @Test
    public void loginSync_serverError_authTokenNotCached() throws Exception{
        when(mLoginHttpEndpointSyncMock.loginSync(anyString(), anyString()))
                .thenReturn(new LoginHttpEndpointSync.EndpointResult(LoginHttpEndpointSync.EndpointResultStatus.SERVER_ERROR, ""));
        SUT.loginSync(USERNAME, PASSWORD);
        verifyNoMoreInteractions(mAuthTokenCacheMock);
    }

    @Test
    public void loginSync_authError_authTokenNotCached() throws Exception{
        when(mLoginHttpEndpointSyncMock.loginSync(anyString(), anyString()))
                .thenReturn(new LoginHttpEndpointSync.EndpointResult(LoginHttpEndpointSync.EndpointResultStatus.AUTH_ERROR, ""));
        SUT.loginSync(USERNAME, PASSWORD);
        verifyNoMoreInteractions(mAuthTokenCacheMock);
    }

    @Test
    public void loginSync_success_loggedInEventPosted() throws Exception{
        ArgumentCaptor<Object> ac = ArgumentCaptor.forClass(Object.class);
        SUT.loginSync(USERNAME, PASSWORD);
        verify(mEventBusPosterMock).postEvent(ac.capture());
        assertTrue(LoginEvent.class.isInstance(ac.getValue()));
    }

    @Test
    public void loginSync_generalError_noInteractionWithEventBusPoster() throws Exception{
        generalError();
        SUT.loginSync(USERNAME, PASSWORD);
        verifyNoMoreInteractions(mEventBusPosterMock);
    }

    @Test
    public void loginSync_serverError_noInteractionWithEventBusPoster() throws Exception{
        when(mLoginHttpEndpointSyncMock.loginSync(anyString(), anyString()))
                .thenReturn(new LoginHttpEndpointSync.EndpointResult(LoginHttpEndpointSync.EndpointResultStatus.SERVER_ERROR, ""));
        SUT.loginSync(USERNAME, PASSWORD);
        verifyNoMoreInteractions(mEventBusPosterMock);
    }

    @Test
    public void loginSync_success_successReturned() throws Exception{
        UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(UseCaseResult.SUCCESS, result);

    }

    @Test
    public void loginSync_networkError_networkErrorReturned() throws Exception {
        doThrow(new NetworkErrorException())
                .when(mLoginHttpEndpointSyncMock).loginSync(anyString(), anyString());
        UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(UseCaseResult.NETWORK_ERROR, result);
    }

}