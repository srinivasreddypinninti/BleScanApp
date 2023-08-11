package com.example.appit.fragment.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MessengerService extends Service {

    private final int MSG_PERFORM_ACTION = 1;

    final Messenger messenger = new Messenger(new InHandler());


    class InHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
           switch (msg.what) {
               case MSG_PERFORM_ACTION:
                   break;
           }
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }
}
