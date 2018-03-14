package com.ef.utils;

public enum DurationType {
    hourly(1),
    daily(24);

    public final int hours;

    DurationType(int hours) {
        this.hours = hours;
    }
}
