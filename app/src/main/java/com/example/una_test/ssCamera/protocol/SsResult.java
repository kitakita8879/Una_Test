package com.example.una_test.ssCamera.protocol;

public class SsResult<T> {
    public final int errorCode;
    public final String errorMsg;
    public final T info;

    public SsResult(int errorCode, String errorMsg, T info) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.info = info;
    }
}
