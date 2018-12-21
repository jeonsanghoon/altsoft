package com.altsoft.Framework;

public enum enResult {
    Request(0), Query(1), Save(2), Close(9);
    private final int value;
    private enResult(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
