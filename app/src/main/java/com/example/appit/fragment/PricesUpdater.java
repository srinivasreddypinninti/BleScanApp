package com.example.appit.fragment;

import java.util.Random;

public class PricesUpdater extends Thread {

    private PriceContainer priceContainer;
    private Random random = new Random();
    public PricesUpdater(PriceContainer priceContainer) {
        this.priceContainer = priceContainer;
    }

    @Override
    public void run() {
        while (true) {
            priceContainer.getLock().lock();

            try {

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                priceContainer.setBitcoinPrice(random.nextInt(20000));
                priceContainer.setEtherPrice(random.nextInt(2000));
                priceContainer.setLiteCoinPrice(random.nextInt(500));
                priceContainer.setBitcoinCashPrice(random.nextInt(5000));
                priceContainer.setBitcoinCashPrice(random.nextDouble());
            }

            finally {
                priceContainer.getLock().unlock();
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
