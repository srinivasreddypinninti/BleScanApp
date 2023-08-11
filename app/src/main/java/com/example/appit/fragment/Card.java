package com.example.appit.fragment;

public class Card {

    private final String name;
    private boolean isActive;

    public Card(String name, boolean isActive) {
        this.name = name;
        this.isActive = isActive;
    }


    public String getName() {
        return name;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
