package com.example.appit.fragment.multithtread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PricesContainer {

    private Lock lockObject = new ReentrantLock();

    private double bitcoinPrice;
    private double etherPrice;
    private double litecoinPrice;
    private double bitcoincashPrice;
    private double ripplePrice;


    public Lock getLockObject() {
        return lockObject;
    }

    public void setLockObject(Lock lockObject) {
        this.lockObject = lockObject;
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

    public double getLitecoinPrice() {
        return litecoinPrice;
    }

    public void setLitecoinPrice(double litecoinPrice) {
        this.litecoinPrice = litecoinPrice;
    }

    public double getBitcoincashPrice() {
        return bitcoincashPrice;
    }

    public void setBitcoincashPrice(double bitcoincashPrice) {
        this.bitcoincashPrice = bitcoincashPrice;
    }

    public double getRipplePrice() {
        return ripplePrice;
    }

    public void setRipplePrice(double ripplePrice) {
        this.ripplePrice = ripplePrice;
    }
}
