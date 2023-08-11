/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.appit.fragment.ble;

import android.Manifest;
import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.AdvertisingSetParameters;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appit.R;
import com.example.appit.fragment.multithtread.Main;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static com.example.appit.fragment.ble.SampleGattAttributes.BLE_NOTIFICATION;
import static com.example.appit.fragment.ble.SampleGattAttributes.CHARACTERISTIC_UUID;
import static com.example.appit.fragment.ble.SampleGattAttributes.SERVICE_UUID;


/**
 * Activity for scanning and displaying available Bluetooth LE devices.
 */
public class DeviceScanActivity extends AppCompatActivity implements DeviceAdapter.OnItemClick{
    //    private LeDeviceListAdapter mLeDeviceListAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean scanning;
    private Handler mHandler;
    public static final String TAG="device_scan_activity";

//    public static final ParcelUuid HEART_RATE_SERVICE_UUID = ParcelUuid.fromString("0000180D-0000-1000-8000-00805f9b34fb");

    private static final int REQUEST_ENABLE_BT = 1;

    private BluetoothManager bluetoothManager;
    private boolean advertising=false;
    private int PERMISSION=255;
    private BluetoothLeScanner bluetoothLeScanner;
    private BluetoothLeAdvertiser bluetoothLeAdvertiser;
    private BluetoothGatt mGattServer;
    private RecyclerView recyclerView;

    private DeviceAdapter deviceAdapter;

//    private Button btnScan;

    private SampleScanCallback mScanCallback;

    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mDeviceServices;

    private HashSet<String> mDeviceList = new HashSet<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getActionBar().setTitle(R.string.title_devices);
        setContentView(R.layout.activity_device_scan);
//        btnScan = findViewById(R.id.btn_qr_scan);
        recyclerView = findViewById(R.id.rv_devices);
        deviceAdapter = new DeviceAdapter(this::onItemClick);

//        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(deviceAdapter);
        mHandler = new Handler(getMainLooper());

        long now = System.currentTimeMillis();
        TimeProfile.getExactTime(now, TimeProfile.ADJUST_NONE);


        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        bluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        bluetoothLeAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();

        if (!isBleSupported()) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private boolean isBleSupported() {
        return mBluetoothAdapter != null && getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }



   /* public void scanQr(View view) {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Scan a barcode or QR Code");
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.initiateScan();
    }*/


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {

//                tvQrText.setVisibility(View.VISIBLE);
//                tvSerial.setVisibility(View.VISIBLE);
//                tvMac.setVisibility(View.VISIBLE);
//                tvIpei.setVisibility(View.VISIBLE);
                // if the intentResult is not null we'll set
                // the content and format of scan message
                //"1|1|MYMODELNO|2|MYSERIAL|3|MYIPEI|4|BT MAC|9"
                String qrString = intentResult.getContents();
                String[] qrArr = qrString.split("\\|");
                //4,6,8

                String deviceMac = qrArr[8];
                findMac(deviceMac);

//                tvQrText.setText(qrString);
//                messageFormat.setText(intentResult.getFormatName());
//                tvSerial.setText("Serial No : "+qrArr[4]);
//                tvIpei.setText("IPEI : "+qrArr[5]);
//                tvMac.setText("MAC : "+qrArr[7]);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private synchronized void findMac(String deviceMac) {

        Log.d(TAG, "findMac mDeviceList size : "+mDeviceList.size());

        if (mDeviceList.size() > 0) {

            for (String scanMac : mDeviceList) {

                if (deviceMac == scanMac) {
                    Log.d(TAG, "findMac: Found Mac..........");
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        if (!scanning) {
            menu.findItem(R.id.menu_stop).setVisible(false);
            menu.findItem(R.id.menu_scan).setVisible(true);
            menu.findItem(R.id.menu_refresh).setActionView(null);
        } else {
            menu.findItem(R.id.menu_stop).setVisible(true);
            menu.findItem(R.id.menu_scan).setVisible(false);
            menu.findItem(R.id.menu_refresh).setActionView(
                    R.layout.actionbar_indeterminate_progress);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_scan:
                startScan();
//                DeviceScannerThread scannerThread = new DeviceScannerThread();
//                scannerThread.start();
                break;
            case R.id.menu_stop:
                stopScan();
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

//        final IntentFilter pairingRequestFilter = new IntentFilter(BluetoothDevice.ACTION_PAIRING_REQUEST);
//        pairingRequestFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY - 1);
//        registerReceiver(mPairingRequestRecevier, pairingRequestFilter);

        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }


    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }*/

    @Override
    protected void onPause() {
        super.onPause();
//        unregisterReceiver(mPairingRequestRecevier);
        stopScan();
    }

    /*private final BroadcastReceiver mPairingRequestRecevier = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (BluetoothDevice.ACTION_PAIRING_REQUEST.equals(intent.getAction()))
            {
                final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                int type = intent.getIntExtra(BluetoothDevice.EXTRA_PAIRING_VARIANT, BluetoothDevice.ERROR);

                if (type == BluetoothDevice.PAIRING_VARIANT_PIN)
                {
//                    device.setPin(Util.IntToPasskey(pinCode()));
                    abortBroadcast();
                }
                else
                {
                    Log.d(TAG, "Unexpected pairing type: " + type);
                }
            }
        }
    };*/



    // scan devices in background thread
    private class DeviceScannerThread extends Thread {
        @Override
        public void run() {
            startScan();
        }
    }



    private void startScan() {

        if (mScanCallback == null) {

            Log.d(TAG, "============ startScan===========: ");
            showToast("...BLE Scan started...");

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                   if (scanning) {
                       stopScan();
                   }
                }
            }, 20000);

            List<ScanFilter> filters = new ArrayList<ScanFilter>();
//            final ParcelUuid pUuid = new ParcelUuid(Constants.MAIN_SERVICE);
            ScanFilter filter = new ScanFilter.Builder()
                    .setServiceUuid(new ParcelUuid(TimeProfile.TIME_SERVICE))
                    .build();
            filters.add(filter);

            ScanSettings settings = new ScanSettings.Builder()
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                    .setReportDelay(2000)
                    .build();


            scanning = true;
            mScanCallback = new SampleScanCallback();
            bluetoothLeScanner.startScan(filters, settings, mScanCallback);
//            bluetoothLeScanner.startScan(null, settings, mScanCallback);



            invalidateOptionsMenu();
        } else {
            showToast("...BLE Scan already started...");
        }
    }

    private void stopScan() {

        scanning = false;
        Log.d(TAG, "run  stopScan -----------: ");
        showToast("-----stop scan----");
        bluetoothLeScanner.stopScan(mScanCallback);
        mScanCallback = null;
        invalidateOptionsMenu();

    }

    private void showToast(String s) {
       runOnUiThread(() -> {
           Toast.makeText(DeviceScanActivity.this, s, Toast.LENGTH_SHORT).show();
       });
    }

    private class SampleScanCallback extends ScanCallback {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            Log.d(TAG, "==== onScanResult ====: " +result.getScanRecord().getDeviceName());

//            stopScan();
//            mDeviceList.add(result.getDevice().getAddress());
            deviceAdapter.addDevice(result, true);
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            Log.d(TAG, "onBatchScanResults: "+results.size());
//            stopScan();
            for(ScanResult result : results) {
//                mDeviceList.add(result.getDevice().getAddress());
                deviceAdapter.addDevice(result, false);
            }
            deviceAdapter.notifyDataSetChanged();
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.e( TAG, "Discovery onScanFailed: " + errorCode );
            showToast("==== onScanFailed ===="+errorCode);
        }
    }

    @Override
    public void onItemClick(ScanResult result) {
        bluetoothLeScanner.stopScan(mScanCallback);
        final BluetoothDevice device = result.getDevice();
        Log.d(TAG, "onItemClick device : " +device.getAddress());

//        mGattServer = device.connectGatt(this, false, mGattCallback, BluetoothDevice.TRANSPORT_LE);
//        close();

        if (device == null) return;
        final Intent intent = new Intent(this, DeviceControlActivity.class);
        intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE, device);
        startActivity(intent);

    }

}