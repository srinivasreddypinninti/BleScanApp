package com.example.appit.fragment.service;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.browse.MediaBrowser;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appit.R;

public class DownloadActivity extends AppCompatActivity {

    private static final String TAG = "DownloadActivity";

    DownloadAckReceiver downloadAckReceiver;
    private DownloadBoundService mService;
    private boolean mBound;

    ProgressBar pbState;
    Button btnDownload;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        btnDownload = findViewById(R.id.button);
        pbState = findViewById(R.id.progressBar);


        Intent intent = new Intent("com.example.appit.fragment.service.LogService");
        intent.setPackage("com.example.appit");
        startService(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, DownloadBoundService.class);
        bindService(intent, boundConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(boundConnection);
        }
    }

    public void startDownload(View view) {
        btnDownload.setEnabled(false);
        pbState.setVisibility(View.VISIBLE);
        Intent serviceIntent = new Intent(this, DownloadService.class);
        serviceIntent.setData(Uri.parse("some string"));
        startService(serviceIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        downloadAckReceiver = new DownloadAckReceiver();
        registerReceiver(downloadAckReceiver, new IntentFilter("DownloadAck"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(downloadAckReceiver);
    }

    public void getRand(View view) {
        if (mBound) {
            String rand = mService.getRand();
            Log.d(TAG, "getRand: "+rand);
            Toast.makeText(this, rand, Toast.LENGTH_LONG).show();
        }
    }

    private final class DownloadAckReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "onReceive: "+ action);
            pbState.setVisibility(View.GONE);
            btnDownload.setEnabled(true);
        }
    }

    private ServiceConnection boundConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            DownloadBoundService.LocalBinder binder = (DownloadBoundService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };
}
