package com.example.appit.userprofile;

import androidx.annotation.Nullable;

import com.example.appit.NetworkErrorException;

import org.junit.Before;
import org.junit.Test;

import static com.example.appit.userprofile.FetchUserProfileUseCaseSync.*;
import static org.junit.Assert.*;

public class FetchUserProfileUseCaseSyncTest {

    public static final String USER_ID = "userId";
    public static final String FULL_NAME = "fullName";
    public static final String IMAGE_URL = "imageUrl";

    UserProfileHttpEndpointSyncTd mUserProfileHttpEndpointSyncTd;
    UserCacheTd mUserCacheTd;
    FetchUserProfileUseCaseSync SUT;

    @Before
    public void setUp() throws Exception {
        mUserProfileHttpEndpointSyncTd = new UserProfileHttpEndpointSyncTd();
        mUserCacheTd = new UserCacheTd();
        SUT = new FetchUserProfileUseCaseSync(mUserProfileHttpEndpointSyncTd, mUserCacheTd);
    }

    //pass userId - to end point

    @Test
    public void fetchUserProfileSync_success_userIdPassed() {
        SUT.fetchUserProfileSync(USER_ID);
        assertEquals(USER_ID, mUserProfileHttpEndpointSyncTd.mUserId);
    }

    // if success - cache user
//    @Test
//    public void fetchUserProfileSync_success_userCached() {
//        SUT.fetchUserProfileSync(USER_ID);
//        User user = mUserCacheTd.getUser(USER_ID);
//        assertEquals(USER_ID, user.getUserId());
//        assertEquals(FULL_NAME, user.getFullName());
//        assertEquals(IMAGE_URL, user.getImageUrl());
//    }


    // server error
//    @Test
//    public void fetchUserProfileSync_serverError_userNotCached() {
//        mUserProfileHttpEndpointSyncTd.isServerError = true;
//        SUT.fetchUserProfileSync(USER_ID);
//        assertNull(mUserCacheTd.getUser(USER_ID));
//    }

    // auth error
//    @Test
//    public void fetchUserProfileSync_authError_userNotCached() {
//        mUserProfileHttpEndpointSyncTd.isAuthError = true;
//        SUT.fetchUserProfileSync(USER_ID);
//        assertNull(mUserCacheTd.getUser(USER_ID));
//    }

    // general error
//    @Test
//    public void fetchUserProfileSync_generalError_userNotCached() {
//        mUserProfileHttpEndpointSyncTd.isGeneralError = true;
//        SUT.fetchUserProfileSync(USER_ID);
//        assertNull(mUserCacheTd.getUser(USER_ID));
//    }

    // success returned
//    @Test
//    public void fetchUserProfileSync_success_successReturned() {
//        UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
//        assertEquals(UseCaseResult.SUCCESS, result);
//    }

    // fail serverError = Failure
    @Test
    public void fetchUserProfileSync_serverError_failureReturned() {
        mUserProfileHttpEndpointSyncTd.isServerError = true;
        UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertEquals(UseCaseResult.FAILURE, result);
    }

    // fail authErr = Failure
//    @Test
//    public void fetchUserProfileSync_authError_failureReturned() {
//        mUserProfileHttpEndpointSyncTd.isAuthError = true;
//        UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
//        assertEquals(UseCaseResult.FAILURE, result);
//    }

    // fail genErr = Failure
//    @Test
//    public void fetchUserProfileSync_generalError_failureReturned() {
//        mUserProfileHttpEndpointSyncTd.isGeneralError = true;
//        UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
//        assertEquals(UseCaseResult.FAILURE, result);
//    }

    // Network error
    @Test
    public void fetchUserProfileSync_networkError_NetworkErrorReturned() {
        mUserProfileHttpEndpointSyncTd.isNetworkError = true;
        UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertEquals(UseCaseResult.NETWORK_ERROR, result);
    }


    public static class UserProfileHttpEndpointSyncTd implements UserProfileHttpEndpointSync {

        public boolean isServerError;
        public boolean isAuthError;
        public boolean isGeneralError;
        public boolean isNetworkError;
        String mUserId;

        @Override
        public EndpointResult getUserProfile(String userId) throws NetworkErrorException {
            mUserId = userId;
            if (isServerError) {
                return new EndpointResult(EndpointResultStatus.SERVER_ERROR, "", "", "");
            } else if (isAuthError) {
                return new EndpointResult(EndpointResultStatus.AUTH_ERROR, "", "", "");
            } else if (isGeneralError) {
                return new EndpointResult(EndpointResultStatus.GENERAL_ERROR, "", "", "");
            } else if (isNetworkError) {
                throw new NetworkErrorException();
            }
            return new EndpointResult(EndpointResultStatus.SUCCESS, userId, FULL_NAME, IMAGE_URL);
        }
    }

    public static class UserCacheTd implements UsersCache {

        User mUser;
        @Override
        public void cacheUser(User user) {
            mUser = user;
        }

        @Nullable
        @Override
        public User getUser(String userId) {
            return mUser;
        }
    }
}