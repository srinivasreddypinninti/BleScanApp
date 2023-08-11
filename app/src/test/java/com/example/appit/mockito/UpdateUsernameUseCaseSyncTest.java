package com.example.appit.mockito;

import com.example.appit.NetworkErrorException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static com.example.appit.mockito.UpdateUsernameUseCaseSync.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class UpdateUsernameUseCaseSyncTest {

    public static final String USER_ID = "userId";
    public static final String USER_NAME = "userName";

    @Mock UpdateUsernameHttpEndpointSync updateUsernameHttpEndpointSyncMoc;
    @Mock  UsersCache mUsersCacheMoc;
    @Mock  EventBusPoster mEventBusPosterMoc;

    UpdateUsernameUseCaseSync SUT;

    @Before
    public void setUp() throws Exception {
//        updateUsernameHttpEndpointSyncMoc = mock(UpdateUsernameHttpEndpointSync.class);
//        mUsersCacheMoc = mock(UsersCache.class);
//        mEventBusPosterMoc = mock(EventBusPoster.class);
        SUT = new UpdateUsernameUseCaseSync(updateUsernameHttpEndpointSyncMoc, mUsersCacheMoc, mEventBusPosterMoc);
        when(updateUsernameHttpEndpointSyncMoc.updateUsername(USER_ID, USER_NAME)).thenReturn(new UpdateUsernameHttpEndpointSync.EndpointResult(UpdateUsernameHttpEndpointSync.EndpointResultStatus.SUCCESS, USER_ID, USER_NAME));

    }

    @Test
    public void updateUserNameSync_userIdAndUserNamePassedToEndpoint() throws Exception{
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        SUT.updateUsernameSync(USER_ID, USER_NAME);
        verify(updateUsernameHttpEndpointSyncMoc, timeout(1)).updateUsername(ac.capture(), ac.capture());
        List<String> arguments = ac.getAllValues();
        assertEquals(USER_ID, arguments.get(0));
        assertEquals(USER_NAME, arguments.get(1));
    }

    @Test
    public void updateUserNameSync_serverError_failureReturned() throws Exception{
        // Arrange
        when(updateUsernameHttpEndpointSyncMoc.updateUsername(USER_ID, USER_NAME))
                .thenReturn(new UpdateUsernameHttpEndpointSync.EndpointResult(UpdateUsernameHttpEndpointSync.EndpointResultStatus.SERVER_ERROR, "", ""));

        // Act
        UseCaseResult result = SUT.updateUsernameSync(USER_ID, USER_NAME);

        // Assert
        assertEquals(UseCaseResult.FAILURE, result);
    }

    @Test
    public void updateUserNameSync_serverError_userNotCached() throws Exception{
        // Arrange
        when(updateUsernameHttpEndpointSyncMoc.updateUsername(USER_ID, USER_NAME)).thenReturn(new UpdateUsernameHttpEndpointSync.EndpointResult(UpdateUsernameHttpEndpointSync.EndpointResultStatus.SERVER_ERROR, "", ""));

        // Act
        SUT.updateUsernameSync(USER_ID, USER_NAME);

        // Assert
        verifyNoMoreInteractions(mUsersCacheMoc);
    }

    @Test
    public void updateUserNameSync_success_userCached() throws Exception{
        // Arrange
        ArgumentCaptor<User> ac = ArgumentCaptor.forClass(User.class);
        // Act
        SUT.updateUsernameSync(USER_ID, USER_NAME);
        verify(mUsersCacheMoc).cacheUser(ac.capture());

        // Assert
        User user = ac.getValue();
        assertEquals(USER_ID, user.getUsername());
        assertEquals(USER_ID, user.getUserId());

    }

    @Test
    public void updateUserNameSync_networkError_networkErrorReturned() throws Exception{
        // Arrange
        doThrow(new NetworkErrorException()).when(updateUsernameHttpEndpointSyncMoc).updateUsername(USER_ID, USER_NAME);
        // Act
        UseCaseResult result = SUT.updateUsernameSync(USER_ID, USER_NAME);
        // Assert
        assertEquals(UseCaseResult.NETWORK_ERROR, result);
    }
}