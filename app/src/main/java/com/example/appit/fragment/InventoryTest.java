package com.example.appit.fragment;

import android.util.Log;

public class InventoryTest {

    private static final String TAG = "InventoryTest";

    IncrementInventory incrementInventoryThread;
    DecrementInventory decrementInventoryThread;
    InventoryCount inventoryCount;
    public InventoryTest() {

        inventoryCount = new InventoryCount();
        incrementInventoryThread = new IncrementInventory(inventoryCount);
        decrementInventoryThread = new DecrementInventory(inventoryCount);

    }

    public void startAction() throws InterruptedException {
        incrementInventoryThread.start();
        decrementInventoryThread.start();

        incrementInventoryThread.join();
        decrementInventoryThread.join();

        Log.d(TAG, "currently have  " + inventoryCount.getItems() + " items.");
    }

    public static class IncrementInventory extends Thread {

        private InventoryCount inventoryCount;

        public IncrementInventory(InventoryCount inventoryCount) {
            this.inventoryCount = inventoryCount;
        }

        @Override
        public void run() {
            for (int i=0; i<10000; i++) {
                inventoryCount.increment();
            }
        }
    }


    public static class DecrementInventory extends Thread {

        private InventoryCount inventoryCount;

        public DecrementInventory(InventoryCount inventoryCount) {
            this.inventoryCount = inventoryCount;
        }

        @Override
        public void run() {
            for (int i=0; i<10000; i++) {
                inventoryCount.decrement();
            }
        }
    }

    private static class InventoryCount {

        private int items = 0;

        Object lock = new Object();

        public void increment() {
            synchronized (lock) {
                items++;
            }
        }

        public void decrement() {
            synchronized (lock) {
                items--;
            }
        }

        public int getItems() {
            synchronized (lock) {
                return items;
            }
        }

    }
}
