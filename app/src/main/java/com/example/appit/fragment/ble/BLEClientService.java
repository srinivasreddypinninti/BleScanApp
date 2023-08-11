package com.example.appit.fragment.ble;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;

import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT8;


public class BLEClientService extends Service {

    private static final String TAG = "CentralService";
    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    public final static String ACTION_GATT_CONNECTED = "ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED = "ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED = "ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE = "ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA = "EXTRA_DATA";


    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mGattServer;

    private final IBinder mBinder = new LocalBinder();
    private int mConnectionState = STATE_DISCONNECTED;

    Random random = new Random();

    /*
    Implements callback methods for GATT events that the app cares about.  For example,
    connection change and services discovered.
    */
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {

            String intentAction;

            if (BluetoothProfile.STATE_CONNECTED == newState) {
                Log.d(TAG, "onConnectionStateChange: STATE_CONNECTED");
                intentAction = ACTION_GATT_CONNECTED;
                mConnectionState = STATE_CONNECTED;
                broadcastUpdate(intentAction);
                Log.i(TAG, "Connected to GATT server.");
                // Attempts to discover services after successful connection.
                Log.i(TAG, "Attempting to start service discovery:" + mGattServer.discoverServices());

            } else if (BluetoothProfile.STATE_DISCONNECTED == newState) {
                Log.d(TAG, "onConnectionStateChange: STATE_DISCONNECTED");
                deleteBondInformation(gatt.getDevice());
//                connectGatt();
                intentAction = ACTION_GATT_DISCONNECTED;
                mConnectionState = STATE_DISCONNECTED;
                Log.i(TAG, "Disconnected from GATT server.");
                broadcastUpdate(intentAction);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Log.d(TAG, "onServicesDiscovered: ");
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.d(TAG, "onCharacteristicRead: ");
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "onCharacteristicRead GATT_SUCCESS : ");
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            } else {
                Log.w(TAG, "onCharacteristicRead GATT_FAILURE");
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            Log.d(TAG, "onCharacteristicChanged: ");
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
        }
    };


    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }


    private void broadcastUpdate(final String action, final BluetoothGattCharacteristic characteristic) {

        Log.d(TAG, "broadcastUpdate: ");
        Intent intent = new Intent(action);

        /*
            This is special handling for the Heart Rate Measurement profile.  Data parsing is
            carried out as per profile specifications:
            http://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.heart_rate_measurement.xml
         */
        if (TimeProfile.CURRENT_TIME.equals(characteristic.getUuid())) {

            int flag = characteristic.getProperties();
            int format = -1;

            if ((flag & 0x01) != 0) {
                format = BluetoothGattCharacteristic.FORMAT_UINT16;
                Log.d(TAG, "data format UINT16.");
            } else {
                format = FORMAT_UINT8;
                Log.d(TAG, "data format UINT8.");
            }

            int msg = characteristic.getIntValue(format, 0);
            Log.d(TAG, String.format("message: %d", msg));
            intent.putExtra(EXTRA_DATA, msg);

        } else {

            /*
            for all other profiles, writes the data formatted in HEX.
            this code isn't relevant for this project.
            */
            final byte[] data = characteristic.getValue();

            if (data != null && data.length > 0) {
                final StringBuilder stringBuilder = new StringBuilder(data.length);
                for (byte byteChar : data) {
                    stringBuilder.append(String.format("%02X ", byteChar));
                }

                Log.w(TAG, "broadcastUpdate. general profile");
                intent.putExtra(EXTRA_DATA, -1);
                //intent.putExtra(EXTRA_DATA, new String(data) + "\n" + stringBuilder.toString());
            }
        }


        sendBroadcast(intent);
    }

    public class LocalBinder extends Binder {
        BLEClientService getService() {
            return BLEClientService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // After using a given device, you should make sure that BluetoothGatt.close() is called
        // such that resources are cleaned up properly.  In this particular example, close() is
        // invoked when the UI is disconnected from the Service.
        close();
        return super.onUnbind(intent);
    }


    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize() {

        // For API level 18 and above, get a reference to BluetoothAdapter through BluetoothManager.
        if (mBluetoothManager == null) {

            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);

            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();

        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }

        return true;
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param device The device address of the destination device.
     *
     * @return Return true if the connection is initiated successfully. The connection result
     *         is reported asynchronously through the
     *         {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     *         callback.
     */
    public boolean connect(final BluetoothDevice device) {

        Log.d(TAG, "connect device address : "+device.getAddress());

//        if (mBluetoothAdapter == null || device == null) {
//            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
//            return false;
//        }

        // Previously connected device.  Try to reconnect.
//        if (mBluetoothDeviceAddress != null && device.getAddress().equals(mBluetoothDeviceAddress) && mGattServer != null) {
//            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
//            if (mGattServer.connect()) {
//                mConnectionState = STATE_CONNECTING;
//                return true;
//            } else {
//                return false;
//            }
//        }

//        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
//        if (device == null) {
//            Log.w(TAG, "Device not found.  Unable to connect.");
//            return false;
//        }

        disconnect();

        mBluetoothDeviceAddress = device.getAddress();
        mConnectionState = STATE_CONNECTING;

        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        mGattServer = device.connectGatt(this, false, mGattCallback);


        Log.d(TAG, "Trying to create a new connection.");

        return true;
    }


    // TODO bluetooth - call this method when needed
    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {

        if (mBluetoothAdapter == null || mGattServer == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }

        mGattServer.disconnect();
    }

    public static void deleteBondInformation(BluetoothDevice device){
        try {
            // FFS Google, just unhide the method.
            Method m = device.getClass().getMethod("removeBond", (Class[]) null);
            m.invoke(device, (Object[]) null);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }




    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public void close() {

        if (mGattServer == null) {
            return;
        }

        mGattServer.close();
        mGattServer = null;
    }



    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {

        if (mBluetoothAdapter == null || mGattServer == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }

        mGattServer.readCharacteristic(characteristic);
    }

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled If true, enable notification.  False otherwise.
     */
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {

        if (mBluetoothAdapter == null || mGattServer == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }

        mGattServer.setCharacteristicNotification(characteristic, enabled);

        // This is specific to Heart Rate Measurement.
        /*
        if (HEART_RATE_MEASUREMENT_UUID.toString().equals(characteristic.getUuid().toString())) {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIGURATION_UUID);
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }
        */

    }

    /**
     * Retrieves a list of supported GATT services on the connected device. This should be
     * invoked only after {@code BluetoothGatt#discoverServices()} completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mGattServer == null) {
            return null;
        }

        return mGattServer.getServices();
    }
}
