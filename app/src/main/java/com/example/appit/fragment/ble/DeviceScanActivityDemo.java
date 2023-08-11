package com.example.appit.fragment.ble;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.pm.PackageManager;
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

public class DeviceScanActivityDemo extends AppCompatActivity {

    private static final String TAG = "DeviceScanActivity";
    public static final ParcelUuid HEART_RATE_SERVICE_UUID = ParcelUuid.fromString("0000180D-0000-1000-8000-00805f9b34fb");

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner bluetoothLeScanner;

    private Handler mHandler;
//    private BluetoothAdapter mBluetoothAdapter;
//    private BluetoothLeScanner mBluetoothLeScanner;
    private boolean mScanning;
    private long SCAN_PERIOD = 10000;

    Button btnUpdate;
    private int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_scan);
        btnUpdate = findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(updateClick);
        mHandler = new Handler(getMainLooper());
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();


    }



    private View.OnClickListener updateClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startScan();
        }

    };


    @Override
    protected void onResume() {
        super.onResume();
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "No BLE Support", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

//        startScan();
//        scanLeDevice(true);
    }


    @Override
    protected void onPause() {
        super.onPause();
//        stopScan();
        bluetoothLeScanner.stopScan(mScanCallback);
//        scanLeDevice(false);
    }

     private void startScan() {

        Log.d(TAG, "============ startScan===========: ");
        Toast.makeText(this, "...BLE Scan started...", Toast.LENGTH_LONG).show();

        List<ScanFilter> filters = new ArrayList<ScanFilter>();

        ScanFilter filter = new ScanFilter.Builder()
                .setServiceUuid(HEART_RATE_SERVICE_UUID)
//				.setServiceUuid( new ParcelUuid(UUID.fromString("CDB7950D-73F1-4D4D-8E47-C090502DBD63")))
                .build();
        filters.add(filter);

        ScanSettings settings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build();

//         bluetoothLeScanner.startScan(mScanCallback);

        bluetoothLeScanner.startScan(filters, settings, mScanCallback);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run  stopScan -----------: ");
                Toast.makeText(DeviceScanActivityDemo.this, "-----stop scan----", Toast.LENGTH_LONG).show();
                bluetoothLeScanner.stopScan(mScanCallback);
            }
        }, 30000);
    }

    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            Log.d(TAG, "==== onScanResult ====: " +result);
            Toast.makeText(DeviceScanActivityDemo.this, "==== onScanResult ====", Toast.LENGTH_LONG).show();
//			if( result == null
//					|| result.getDevice() == null
//					|| TextUtils.isEmpty(result.getDevice().getName()) )
//				return;

            StringBuilder builder = new StringBuilder( result.getDevice().getName() );

            builder.append("\n").append(new String(result.getScanRecord()
                    .getServiceData(result.getScanRecord().getServiceUuids().get(0)), Charset.forName("UTF-8")));

            Log.d(TAG, "==== onScanResult ====: "+builder.toString());
//			mText.setText(builder.toString());
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.e( TAG, "Discovery onScanFailed: " + errorCode );
            Toast.makeText(DeviceScanActivityDemo.this, "==== onScanFailed ===="+errorCode, Toast.LENGTH_LONG).show();
        }
    };

    /*private void startScan() {

        ScanFilter filter = new ScanFilter.Builder()
//                .setDeviceAddress("58:d3:91:92:49:2d")
//                .setServiceUuid(HEART_RATE_SERVICE_UUID)
                .build();

        ArrayList<ScanFilter> filters = new ArrayList<>();
        filters.add(filter);


        ScanSettings settings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build();

        Log.d(TAG, "startScan bluetoothLeScanner : "+bluetoothLeScanner);

        Toast.makeText(this, "Scan started...", Toast.LENGTH_LONG).show();

        bluetoothLeScanner.startScan(filters, settings, scanCallback);
//        bluetoothLeScanner.startScan(scanCallback);
    }

    private void stopScan() {
        Log.d(TAG, "stopScan: ");
        bluetoothLeScanner.stopScan(scanCallback);
    }*/

    /*private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            Log.d(TAG, "onScanResult: ");
            Toast.makeText(DeviceScanActivity.this, "onScanResult...", Toast.LENGTH_LONG).show();
            processResult(result);
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            Log.d(TAG, "onBatchScanResults: ");
            for (ScanResult scanResult : results) {
                processResult(scanResult);
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.d(TAG, "onScanFailed: ");
        }
    };*/

    /*private void processResult(ScanResult result) {
        Log.d(TAG, "New LE Device: " + result.getDevice().getName());
        Toast.makeText(DeviceScanActivity.this, "processResult..."+result.getDevice().getName(), Toast.LENGTH_LONG).show();
    }

    private void scanLeDevice(final boolean enable) {
        Toast.makeText(this, "scanLeDevice started...", Toast.LENGTH_LONG).show();
        if (enable) {
            // Stops scanning after a pre-defined scan period.
//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mScanning = false;
//                    Toast.makeText(DeviceScanActivity.this, "Stop scan...", Toast.LENGTH_LONG).show();
//                    bluetoothLeScanner.stopScan(scanCallback);
////					invalidateOptionsMenu();
//                }
//            }, SCAN_PERIOD);

            mScanning = true;
            Toast.makeText(this, "startScan...", Toast.LENGTH_LONG).show();
            bluetoothLeScanner.startScan(scanCallback);
        } else {
            mScanning = false;
            Toast.makeText(this, "stopScan...", Toast.LENGTH_LONG).show();
            bluetoothLeScanner.stopScan(scanCallback);
        }
//		invalidateOptionsMenu();
    }

    // Device scan callback.
    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            Log.d(TAG, "onScanResult: ");
            Toast.makeText(DeviceScanActivity.this, "onScanResult...", Toast.LENGTH_LONG).show();
            processResult(result);
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            Log.d(TAG, "onBatchScanResults results : " +results.size());
            Toast.makeText(DeviceScanActivity.this, "onBatchScanResults...", Toast.LENGTH_LONG).show();
            for (ScanResult result : results) {
                processResult(result);
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.d(TAG, "onScanFailed: ");
            Toast.makeText(DeviceScanActivity.this, "onScanFailed...", Toast.LENGTH_LONG).show();
        }
    };*/

//    private void processResult(ScanResult result) {
//        Log.d(TAG, "New LE Device : " + result.getDevice().getName() + " @ " + result.getRssi());
//    }
}