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

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appit.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.appit.fragment.ble.Constants.SERVER_MSG_FIRST_STATE;
import static com.example.appit.fragment.ble.Constants.SERVER_MSG_SECOND_STATE;

/**
 * For a given BLE device, this Activity provides the user interface to connect, display data,
 * and display GATT services and characteristics supported by the device.  The Activity
 * communicates with {@code BluetoothLeService}, which in turn interacts with the
 * Bluetooth LE API.
 */
public class DeviceControlActivity extends Activity implements View.OnClickListener {
    private final static String TAG = DeviceControlActivity.class.getSimpleName();

    public static final String EXTRAS_DEVICE = "DEVICE";
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    private static final String LIST_NAME = "NAME";
    private static final String LIST_UUID = "UUID";


    private BLEClientService mBluetoothLeService;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mDeviceServices;
    private BluetoothGattCharacteristic mCharacteristic;

    private BluetoothDevice mBluetoothDevice;
//    private String mDeviceName;
//    private String mDeviceAddress;

    private TextView mConnectionStatus;
    private TextView mConnectedDeviceName;
//    private TextView tvDay;
    private ImageView mServerCharacteristic;
    private Button mRequestReadCharacteristic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gatt_services_characteristics);
        mDeviceServices = new ArrayList<>();
        mCharacteristic = null;


        Intent intent = getIntent();
        if (intent != null) {
            mBluetoothDevice = intent.getParcelableExtra(EXTRAS_DEVICE);
//            mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
//            mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
        }


        mConnectionStatus = (TextView) findViewById(R.id.connection_status);
        mConnectedDeviceName = (TextView) findViewById(R.id.connected_device_name);

        mServerCharacteristic = (ImageView) findViewById(R.id.server_characteristic_value);
//        tvDay = (TextView) findViewById(R.id.tv_dayOfWeek);
        mRequestReadCharacteristic = (Button) findViewById(R.id.request_read_characteristic);
        mRequestReadCharacteristic.setOnClickListener(this);


//        mConnectedDeviceName.setText(mBluetoothDevice.getName());
        if (TextUtils.isEmpty(mBluetoothDevice.getName())) {
            mConnectedDeviceName.setText("IQPANEL-BTD");
        } else {
            mConnectedDeviceName.setText(mBluetoothDevice.getName());
        }


        Intent gattServiceIntent = new Intent(this, BLEClientService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

        updateConnectionState(R.string.connected);
        mRequestReadCharacteristic.setEnabled(true);
        updateInputFromServer(SERVER_MSG_SECOND_STATE);
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }


    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.request_read_characteristic:
                requestReadCharacteristic();
                break;

        }
    }


    /*
    request from the Server the value of the Characteristic.
    this request is asynchronous.
     */
    private void requestReadCharacteristic() {
        if (mBluetoothLeService != null && mCharacteristic != null) {
            mBluetoothLeService.readCharacteristic(mCharacteristic);
        } else {
//            showMsgText(R.string.error_unknown);
        }
    }


    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {

            mBluetoothLeService = ((BLEClientService.LocalBinder) service).getService();

            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }

            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mBluetoothDevice);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };


    /*
     Handles various events fired by the Service.
     ACTION_GATT_CONNECTED: connected to a GATT server.
     ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
     ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
     ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read or notification operations.
    */
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if (action == null) {
                return;
            }

            switch (intent.getAction()) {

                case BLEClientService.ACTION_GATT_CONNECTED:
                    updateConnectionState(R.string.connected);
                    mRequestReadCharacteristic.setEnabled(true);
                    break;

                case BLEClientService.ACTION_GATT_DISCONNECTED:
                    updateConnectionState(R.string.disconnected);
//                    mRequestReadCharacteristic.setEnabled(false);
                    break;


                case BLEClientService.ACTION_GATT_SERVICES_DISCOVERED:
                    // set all the supported services and characteristics on the user interface.
                    Log.d(TAG, "=======ACTION_GATT_SERVICES_DISCOVERED============: ");
                    Toast.makeText(DeviceControlActivity.this, "GATT SERVICES DISCOVERED", Toast.LENGTH_SHORT).show();
                    setGattServices(mBluetoothLeService.getSupportedGattServices());
                    registerCharacteristic();
                    break;

                case BLEClientService.ACTION_DATA_AVAILABLE:
                    int msg = intent.getIntExtra(BLEClientService.EXTRA_DATA, -1);
                    Log.d(TAG, "ACTION_DATA_AVAILABLE " + msg);
                    updateInputFromServer(msg);
                    break;

            }
        }
    };


    /*
     This sample demonstrates 'Read' and 'Notify' features.
     See http://d.android.com/reference/android/bluetooth/BluetoothGatt.html for the complete
     list of supported characteristic features.
    */
    private void registerCharacteristic() {

        Log.d(TAG, "registerCharacteristic: ");

        BluetoothGattCharacteristic characteristic = null;

        if (mDeviceServices != null) {

            /* iterate all the Services the connected device offer.
            a Service is a collection of Characteristic.
             */
            for (ArrayList<BluetoothGattCharacteristic> service : mDeviceServices) {

                // iterate all the Characteristic of the Service
                for (BluetoothGattCharacteristic serviceCharacteristic : service) {

                    /* check this characteristic belongs to the Service defined in
                    PeripheralAdvertiseService.buildAdvertiseData() method
                     */
                    if (serviceCharacteristic.getService().getUuid().equals(TimeProfile.TIME_SERVICE)) {

                        if (serviceCharacteristic.getUuid().equals(TimeProfile.CURRENT_TIME)) {
                            characteristic = serviceCharacteristic;
                            mCharacteristic = characteristic;
                        }
                    }
                }
            }

           /*
            int charaProp = characteristic.getProperties();
            if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
            */

            if (characteristic != null) {
                mBluetoothLeService.readCharacteristic(characteristic);
                mBluetoothLeService.setCharacteristicNotification(characteristic, true);
            }
        }
    }


    /*
    Demonstrates how to iterate through the supported GATT Services/Characteristics.
    */
    private void setGattServices(List<BluetoothGattService> gattServices) {

        if (gattServices == null) {
            return;
        }

        mDeviceServices = new ArrayList<>();

        // Loops through available GATT Services from the connected device
        for (BluetoothGattService gattService : gattServices) {
            ArrayList<BluetoothGattCharacteristic> characteristic = new ArrayList<>();
            characteristic.addAll(gattService.getCharacteristics()); // each GATT Service can have multiple characteristic
            mDeviceServices.add(characteristic);
        }

    }


    private void updateConnectionState(final int resourceId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mConnectionStatus.setText(resourceId);
            }
        });
    }


    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BLEClientService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BLEClientService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BLEClientService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BLEClientService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }


    private void updateInputFromServer(int msg) {

        String color;

        switch (msg) {
            case SERVER_MSG_FIRST_STATE:
                color = "#AD1457";
                break;

            case SERVER_MSG_SECOND_STATE:
                color = "#6A1B9A";
                break;

            default:
                color = "#FFFFFF";
                break;

        }

        mServerCharacteristic.setBackgroundColor(Color.parseColor(color));
//        showMsgText(String.format(getString(R.string.characteristic_value_received), msg));
    }
}
