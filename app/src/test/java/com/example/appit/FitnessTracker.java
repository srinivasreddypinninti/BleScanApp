package com.example.appit;

public class FitnessTracker {

    public static final int RUN_STEP_FACTOR = 2;

    public void step() {
        Counter.getInstance().add();
    }

    public void runStep() {
        Counter.getInstance().add(RUN_STEP_FACTOR);
    }

    public int getTotalSteps() {
        return  Counter.getInstance().getTotal();
    }
}
