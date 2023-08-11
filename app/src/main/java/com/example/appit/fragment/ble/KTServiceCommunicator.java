package com.example.appit.fragment.ble;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;


public class KTServiceCommunicator {

    private static final String TAG = "KTServiceCommunicator";

    final String BASE_URL =
            "https://jsonplaceholder.typicode.com";

    private static final String KT_BASE_URL = "http://10.11.7.249:8801/SmartService";

    public enum Event {
        DISARM, ARM_STAY, ARM_AWAY
    }

    private BlockingQueue<Event> blockingQueue = new ArrayBlockingQueue(5);

    private Executor executor;

    public KTServiceCommunicator(Executor executor) {
        this.executor = executor;
    }

    public void makeLoginRequest(String name, String password) {
        executor.execute(() -> {
            login(name, password);
//           notifyResult(result, callback);
        });
    }

    public static void login(String userName, String password) {

        final String USER_NAME = "userName";
        final String PASSWORD = "password";
//        final String SOURCE_ID = "SourceId";
//        final String ENCRYPTED = "encrypted";
//        final String BYPASS_LANG = "bypassLanguageCode";

//        String sdKey = "e8847343-4c48-44c9-8746-405b3756dd49";
        Uri builtUri = Uri.parse(KT_BASE_URL)
                .buildUpon()
                .appendPath("Login")
                .appendQueryParameter(USER_NAME, "gaurav")
                .appendQueryParameter(PASSWORD, "krishna@1")
//                .appendQueryParameter(SOURCE_ID, "SourceId")
//                .appendQueryParameter(ENCRYPTED, "encrypted")
//                .appendQueryParameter(BYPASS_LANG, "bypassLanguageCode")
                .build();

        String myUrl = builtUri.toString();

        Log.d(TAG, "login myUrl: " + myUrl);
        //login myUrl: https://127.0.0.1:8801/SmartService/Login?userName=userName
        // &password=password
        // &SourceId=SourceId
        // &encrypted=encrypted
        // &bypassLanguageCode=bypassLanguageCode

        try {
            URL url = new URL(myUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer sb = new StringBuffer();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                Log.d(TAG, "login response : " + sb.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}