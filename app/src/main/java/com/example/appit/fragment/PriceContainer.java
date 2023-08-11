package com.example.appit.fragment;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PriceContainer {

    private Lock lock = new ReentrantLock();

    private double bitcoinPrice;
    private double etherPrice;
    private double liteCoinPrice;
    private double bitcoinCashPrice;
    private double ripplePrice;

    public Lock getLock() {
        return lock;
    }

    public void setLock(Lock lock) {
        this.lock = lock;
    }

    public double getBitcoinPrice() {
        return bitcoinPrice;
    }

    public void setBitcoinPrice(double bitcoinPrice) {
        this.bitcoinPrice = bitcoinPrice;
    }

    public double getEtherPrice() {
        return etherPrice;
    }

    public void setEtherPrice(double etherPrice) {
        this.etherPrice = etherPrice;
    }

    public double getLiteCoinPrice() {
        return liteCoinPrice;
    }

    public void setLiteCoinPrice(double liteCoinPrice) {
        this.liteCoinPrice = liteCoinPrice;
    }

    public double getBitcoinCashPrice() {
        return bitcoinCashPrice;
    }

    public void setBitcoinCashPrice(double bitcoinCashPrice) {
        this.bitcoinCashPrice = bitcoinCashPrice;
    }

    public double getRipplePrice() {
        return ripplePrice;
    }

    public void setRipplePrice(double ripplePrice) {
        this.ripplePrice = ripplePrice;
    }
}
