package com.example.appit.fragment.ble;

import android.os.ParcelUuid;

import java.util.UUID;

public class Constants {

    public static final int SERVER_MSG_FIRST_STATE = 1;
    public static final int SERVER_MSG_SECOND_STATE = 2;

    public static final UUID HEART_RATE_UUID = UUID.fromString("0000180D-0000-1000-8000-00805f9b34fb");
    public static final ParcelUuid HEART_RATE_SERVICE_UUID = ParcelUuid.fromString("0000180D-0000-1000-8000-00805f9b34fb");
    public static final UUID BODY_SENSOR_LOCATION_CHARACTERISTIC_UUID = UUID.fromString("00002A38-0000-1000-8000-00805f9b34fb");


    private final static String BASE_UUID = "0000%s-0000-1000-8000-00805f9b34fb";


    public final static UUID MAIN_SERVICE = UUID.fromString(String.format(BASE_UUID, "FEE0"));
    public final static UUID CHARAC_REQUEST = UUID.fromString(String.format(BASE_UUID, "FEE01"));
    public final static UUID CHARAC_RECORDING = UUID.fromString(String.format(BASE_UUID, "FEE02"));
}
