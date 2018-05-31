package com.treblemaker.weighters.enums;

public enum WeightClass {
    BAD(0),OK(1),GOOD(2);

    private final int value;
    WeightClass(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}