package com.example.appit;


import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Test;

import static com.example.appit.LoginUseCaseSync.*;
import static org.junit.Assert.*;

public class LoginUseCaseSyncTest {

    public static final String USERNAME = "userName";
    public static final String PASSWORD = "password";
    public static final String AUTHTOKEN = "authToken";
    LoginHttpEndpointSyncTd mEndpointSyncTd;
    AuthTokenCacheTd mAuthTokenCacheTd;
    EventBusPosterTd mEventBusPosterTd;

    LoginUseCaseSync SUT;

    @Before
    public void setUp() throws Exception {
        mEndpointSyncTd = new LoginHttpEndpointSyncTd();
        mAuthTokenCacheTd = new AuthTokenCacheTd();
        mEventBusPosterTd = new EventBusPosterTd();
        SUT = new LoginUseCaseSync(mEndpointSyncTd, mAuthTokenCacheTd, mEventBusPosterTd);
    }

    // userName, password passed to endpoint

    @Test
    public void loginSync_success_userNamePasswordPassed() throws NetworkErrorException{
        SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(USERNAME, mEndpointSyncTd.userName);
        assertEquals(PASSWORD, mEndpointSyncTd.password);
    }

    // if login success auth token must be cached
    @Test
    public void loginSync_success_authTokenCached() throws NetworkErrorException{
        SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(AUTHTOKEN, mAuthTokenCacheTd.getAuthToken());
    }

    // if login fail general error - auth token not changed
    @Test
    public void loginSync_general_error_authTokenNotChanged() throws NetworkErrorException{
        mEndpointSyncTd.isGeneralError = true;
        SUT.loginSync(USERNAME, PASSWORD);
        assertEquals("", mAuthTokenCacheTd.getAuthToken());
    }

    // if login fail server error - auth token not changed
    @Test
    public void loginSync_server_error_authTokenNotChanged() throws NetworkErrorException{
        mEndpointSyncTd.isServerError = true;
        SUT.loginSync(USERNAME, PASSWORD);
        assertEquals("", mAuthTokenCacheTd.getAuthToken());
    }

    // if login fail auth error - auth token not changed
    @Test
    public void loginSync_auth_error_authTokenNotChanged() throws NetworkErrorException{
        mEndpointSyncTd.isAuthError = true;
        SUT.loginSync(USERNAME, PASSWORD);
        assertEquals("", mAuthTokenCacheTd.getAuthToken());
    }

    // if login success - login event post to event bus


    @Test
    public void loginSync_success_loginEventPostToEventBus() throws NetworkErrorException{
        SUT.loginSync(USERNAME, PASSWORD);
        Object obj = mEventBusPosterTd.loginEvent;
        assertEquals(true, LoginEvent.class.isInstance(obj));
    }

    // if login fails general error - no login event post to event bus

    @Test
    public void loginSync_general_error_noInteractionWithEventBus() throws NetworkErrorException{
        mEndpointSyncTd.isGeneralError = true;
        SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(0, mEventBusPosterTd.eventPostedCount);
    }

    // if login fails server error - no login event post to event bus

    @Test
    public void loginSync_server_error_noInteractionWithEventBus() throws NetworkErrorException{
        mEndpointSyncTd.isServerError = true;
        SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(0, mEventBusPosterTd.eventPostedCount);
    }

    // if login fails auth error - no login event post to event bus

    @Test
    public void loginSync_auth_error_noInteractionWithEventBus() throws NetworkErrorException{
        mEndpointSyncTd.isAuthError = true;
        SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(0, mEventBusPosterTd.eventPostedCount);
    }

    // if login succeed - success returned
    @Test
    public void loginSync_success_successReturned() throws NetworkErrorException{
        UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(UseCaseResult.SUCCESS, result);
    }

    // if login fails - failure returned
    @Test
    public void loginSync_serverError_failureReturned() throws NetworkErrorException{
        mEndpointSyncTd.isServerError = true;
        UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(UseCaseResult.FAILURE, result);
    }

    // if login fails - auth error failure returned
    @Test
    public void loginSync_authError_failureReturned() throws NetworkErrorException{
        mEndpointSyncTd.isAuthError = true;
        UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(UseCaseResult.FAILURE, result);
    }

    // if login fails - general error failure returned
    @Test
    public void loginSync_generalError_failureReturned() throws NetworkErrorException{
        mEndpointSyncTd.isGeneralError = true;
        UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(UseCaseResult.FAILURE, result);
    }

    // network error - network error returned
    @Test
    public void loginSync_networkError_networkErrorReturned() throws NetworkErrorException{
        mEndpointSyncTd.isNetworkError = true;
        UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(UseCaseResult.NETWORK_ERROR, result);
    }

    public static class LoginHttpEndpointSyncTd implements LoginHttpEndpointSync {
        public String userName;
        private String password;

        boolean isNetworkError;
        boolean isGeneralError;
        boolean isServerError;
        boolean isAuthError;

        @Override
        public EndpointResult loginSync(String userName, String password) throws NetworkErrorException {
            this.userName = userName;
            this.password = password;
            if (isGeneralError) {
                return new EndpointResult(EndpointResultStatus.GENERAL_ERROR, "");
            } else if (isServerError) {
                return new EndpointResult(EndpointResultStatus.SERVER_ERROR, "");
            } else if (isAuthError) {
                return new EndpointResult(EndpointResultStatus.AUTH_ERROR, "");
            } else if (isNetworkError) {
                throw new NetworkErrorException();
            }
            return new EndpointResult(EndpointResultStatus.SUCCESS, AUTHTOKEN);
        }
    }

    public static class AuthTokenCacheTd implements AuthTokenCache {

        private String authToken = "";
        @Override
        public void cacheAuthToken(String auth) {
            authToken = auth;
        }

        @Override
        public String getAuthToken() {
            return authToken;
        }
    }

    public static class EventBusPosterTd implements EventBusPoster {

        int eventPostedCount = 0;
        Object loginEvent;
        @Override
        public void postEvent(Object event) {
            eventPostedCount++;
            loginEvent = event;
        }
    }
}