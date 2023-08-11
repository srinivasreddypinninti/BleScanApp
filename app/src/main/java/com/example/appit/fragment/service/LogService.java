package com.example.appit.fragment.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

public class LogService extends Service {

    private static final String TAG = "LOGGER";
    public LogService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        final  String version = intent.getStringExtra("version");
        return new ILogService.Stub() {
            @Override
            public void log_d(String tag, String message) throws RemoteException {
                Log.d(TAG, " tag "+tag + ", message "+message+ ", version : "+version);
            }

            @Override
            public void log(Message message) throws RemoteException {
                Log.d(TAG, " message "+message);
            }
        };
    }
}