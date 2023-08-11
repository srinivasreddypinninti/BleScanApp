package com.example.appit.fragment.multithtread;

import android.util.Log;

import com.example.appit.fragment.PriceContainer;

import java.util.Random;

public class PriceUpdaterThread extends Thread{

    private static final String TAG = "PriceUpdaterThread";

    private PricesContainer pricesContainer;
    private Random random = new Random();

    public PriceUpdaterThread(PricesContainer pricesContainer) {
        this.pricesContainer = pricesContainer;
    }

    @Override
    public void run() {

        while (true) {

            Log.d(TAG, "run: accquire the lock...");
            pricesContainer.getLockObject().lock();

            try {

//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

                pricesContainer.setBitcoinPrice(random.nextInt(20000));
                pricesContainer.setEtherPrice(random.nextInt(2000));
                pricesContainer.setLitecoinPrice(random.nextInt(500));
                pricesContainer.setBitcoincashPrice(random.nextInt(5000));
                pricesContainer.setRipplePrice(random.nextDouble());
            }
             finally {
                Log.d(TAG, "run: release the lock...");
                pricesContainer.getLockObject().unlock();
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
