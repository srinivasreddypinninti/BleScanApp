package com.example.appit.fragment.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.Random;

public class DownloadBoundService extends Service {


    private final Random random = new Random();
    private final LocalBinder binder = new LocalBinder();


    public class LocalBinder extends Binder {
        DownloadBoundService getService() {
            return DownloadBoundService.this;
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public String getRand() {
        return String.valueOf(random.nextInt(100));
    }
}
