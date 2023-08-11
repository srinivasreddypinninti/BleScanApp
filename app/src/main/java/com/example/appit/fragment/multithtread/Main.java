package com.example.appit.fragment.multithtread;

import android.util.Log;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

public class Main {

    private static final String TAG = "Main";

    public static final int MAX_PWD = 9999;

    public void start() {

        InventoryCounter inventoryCounter = new InventoryCounter();
        IncrementThread incrementThread = new IncrementThread(inventoryCounter);
        DecrementThread decrementThread = new DecrementThread(inventoryCounter);

        incrementThread.start();
        decrementThread.start();
        try {
            incrementThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        try {
            decrementThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "currently available items : "+inventoryCounter.getItems());

    }

    private static class InventoryCounter {

        Object lock1 = new Object();
        Object lock2 = new Object();
        private long items = 0;

        public void increment() {
            synchronized (this.lock1) {
                items++;
            }
        }

        public void decrement() {
            synchronized (this.lock1) {
                items--;
            }
        }

        public long getItems() {
                return items;
        }

    }


    private static class IncrementThread extends Thread {

        private final InventoryCounter inventoryCounter;

        public IncrementThread(InventoryCounter inventoryCounter) {
            this.inventoryCounter = inventoryCounter;
        }

        @Override
        public void run() {

            for (int i = 0; i < 1000; i++) {
                inventoryCounter.increment();
            }

        }
    }

    private static class DecrementThread extends Thread {

        private final InventoryCounter inventoryCounter;

        public DecrementThread(InventoryCounter inventoryCounter) {
            this.inventoryCounter = inventoryCounter;
        }

        @Override
        public void run() {

            for (int i = 0; i < 1000; i++) {
                inventoryCounter.decrement();
            }

        }
    }

}
