package com.example.appit.fragment.ble;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.AdvertisingSet;
import android.bluetooth.le.AdvertisingSetCallback;
import android.bluetooth.le.AdvertisingSetParameters;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.appit.R;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class AdvertiserActivity extends AppCompatActivity {

    private static final String TAG = "AdvertiserActivity";

    public static final ParcelUuid HEART_RATE_SERVICE_UUID = ParcelUuid.fromString("0000180D-0000-1000-8000-00805f9b34fb");

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothLeAdvertiser bluetoothLeAdvertiser;

    private Random random = new Random();
    private Handler mHandler = new Handler(getMainLooper());
    private BluetoothLeScanner bluetoothLeScanner;

    Button btnUpdate;
//    private AdvertisingSet currentAdvertisingSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertiser);
        btnUpdate = findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(updateClick);

        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
//        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
        bluetoothLeAdvertiser = bluetoothAdapter.getBluetoothLeAdvertiser();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "No BLE Support", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        startAdvertising();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopAdvertising();
//        bluetoothLeScanner.stopScan(mScanCallback);
        Toast.makeText(this, "...stop scan...", Toast.LENGTH_LONG).show();
    }

    private View.OnClickListener updateClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            stopAdvertising();
            startAdvertising();
        }

    };



    private void startAdvertising() {
        BluetoothLeAdvertiser advertiser = BluetoothAdapter.getDefaultAdapter().getBluetoothLeAdvertiser();

        AdvertiseSettings settings = new AdvertiseSettings.Builder()
                .setAdvertiseMode( AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY )
                .setTxPowerLevel( AdvertiseSettings.ADVERTISE_TX_POWER_HIGH )
                .setTimeout(0)
                .setConnectable(false)
                .build();

//		ParcelUuid pUuid = new ParcelUuid( UUID.fromString("CDB7950D-73F1-4D4D-8E47-C090502DBD63"));

        AdvertiseData data = new AdvertiseData.Builder()
                .setIncludeDeviceName(true)
                .addServiceUuid(HEART_RATE_SERVICE_UUID)
//                .addServiceData( pUuid, "Data".getBytes(Charset.forName("UTF-8") ) )
                .addServiceData(HEART_RATE_SERVICE_UUID, buildTempPacket())
                .build();



        advertiser.startAdvertising( settings, data, advertisingCallback );
    }


    private void stopAdvertising() {
        if (bluetoothLeAdvertiser == null) return;

        Log.d(TAG, "stopAdvertising: ");
        bluetoothLeAdvertiser.stopAdvertising(advertisingCallback);
    }

    AdvertiseCallback advertisingCallback = new AdvertiseCallback() {
        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            super.onStartSuccess(settingsInEffect);
            Log.d(TAG, "onStartSuccess: ");
            Toast.makeText(AdvertiserActivity.this, "....BLE Advertising...", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onStartFailure(int errorCode) {
            Log.e( "BLE", "Advertising onStartFailure: " + errorCode );
            super.onStartFailure(errorCode);
            Toast.makeText(AdvertiserActivity.this, "BLE Advertising Failed."+errorCode, Toast.LENGTH_LONG).show();
        }
    };



    private byte[] buildTempPacket() {

//		int n = random.nextInt();
//		byte[] bytes = {0x00, 0x1};
        return new byte[] {(byte)10, 0x00};
//		return bytes;
    }
}