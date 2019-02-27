package com.altsoft.Framework;

public enum enResult {
    BannerRequest(0), LocalboxRequest(1),  LoglstationRequest(2), Close(99),  LoginRequest(11),MemberJoin(12),FindPassword(13);
    private final int value;
    private enResult(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
