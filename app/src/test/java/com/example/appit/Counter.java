package com.example.appit;

public class Counter {

    public static Counter instance;

    private int step = 0;

    public static Counter getInstance() {
        if (instance == null) {
            synchronized(Counter.class) {
                if (instance == null) {
                    instance = new Counter();
                }
            }
        }
        return instance;
    }

    public void add() {
        step++;
    }

    public void add(int factor) {
        step = step + factor;
    }

    public int getTotal() {
        return  step;
    }
}
