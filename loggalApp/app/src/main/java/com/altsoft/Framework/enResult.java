package com.altsoft.Framework;

public enum enResult {
    BannerRequest(0), LocalboxRequest(1), LoginRequest(2), Close(99), MemberJoin(11);
    private final int value;
    private enResult(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
