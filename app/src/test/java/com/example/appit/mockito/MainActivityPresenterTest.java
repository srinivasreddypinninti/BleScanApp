package com.example.appit.mockito;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Handler;
import android.widget.Button;

import com.example.appit.LoadJsonInterface;
import com.example.appit.MainActivity;
import com.example.appit.NetworkErrorException;
import com.example.appit.R;
import com.example.appit.mockito.MainActivityPresenter;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MainActivityPresenterTest {


    MainActivityPresenter SUT;

    @Mock
    MainActivity mainActivity;

    @Mock
    Button buttonMock;

    @Mock
    Handler mockHandler;

    @Before
    public void setUp() throws Exception {

        when(mainActivity.findViewById(R.id.button2)).thenReturn(buttonMock);

        SUT = new MainActivityPresenter(mainActivity);
        // when(mContextMock.getApplicationContext().getAssets()).thenReturn(assetManagerMock);

    }

    @Test
    public void fetchContacts() throws Exception {
        ArgumentCaptor<Runnable> ac = ArgumentCaptor.forClass(Runnable.class);
        SUT.fetchData();
        verify(mainActivity).runOnUiThread(ac.capture());
        ac.getValue().run();
    }

    @Test
    public void fetchData_Handler() throws Exception {
        Handler mockHandler = mock(Handler.class);
//        when(mockHandler.postDelayed(any(Runnable.class), anyLong())).thenAnswer(new Answer() {
//
//            @Override
//            public Object answer(InvocationOnMock invocation) throws Throwable {
//                Runnable runnable = invocation.getArgument(0);
//                        runnable.run();
//                return null;
//            }
//
//        });
        Thread thread = mock(Thread.class);
        doNothing().when(thread).sleep(anyLong());
        SUT.fetchData_Handler(mockHandler);
//        Field button = MainActivityPresenter.class.getDeclaredField("button");
//        button.setAccessible(true);
//        Button btn = (Button) button.get(SUT);
//        verify(btn).setEnabled(true);
    }

}