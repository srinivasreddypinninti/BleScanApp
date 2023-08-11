package com.example.appit.fragment.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DownloadService extends Service {

    private static final String TAG = "DownloadService";

    private Looper mServiceLooper;
    private Handler mServiceHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread thread = new HandlerThread(DownloadService.class.getName());
        thread.start();

        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "onStartCommand: "+Thread.currentThread().getName());

        Message message = mServiceHandler.obtainMessage();
        message.arg1 = startId;
        message.obj = intent;
        mServiceHandler.sendMessage(message);

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mServiceLooper.quitSafely();
    }

    private final class ServiceHandler extends Handler {

        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.d(TAG, "handleMessage: "+Thread.currentThread().getName());
            downloadImage((Intent)msg.obj);

            stopSelf(msg.arg1);
        }

        private void downloadImage(Intent intent) {
            Log.d(TAG, "downloadImage: ");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Log.d(TAG, "sendBroadcast.. ");
            // notify client
            Intent ackIntent = new Intent("DownloadAck");
            sendBroadcast(ackIntent);
        }
    }
}
