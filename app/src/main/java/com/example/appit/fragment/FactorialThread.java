package com.example.appit.fragment;

import java.math.BigInteger;
import java.nio.file.Files;

public class FactorialThread extends Thread{

    private final long inputNumber;
    private BigInteger result = BigInteger.ZERO;
    private boolean isFinished;

    public FactorialThread(long inputNumber) {
        this.inputNumber = inputNumber;

    }

    @Override
    public void run() {
        this.result = factorial(inputNumber);
        this.isFinished = true;
    }

    private BigInteger factorial(long inputNumber) {
        BigInteger tempResult = BigInteger.ONE;

        for (long i = inputNumber; i >= 1; i--) {
            tempResult = tempResult.multiply(new BigInteger(String.valueOf(i)));
        }
        return tempResult;
    }

    public BigInteger getResult() {
        return result;
    }

    public boolean isFinished() {
        return isFinished;
    }
}
