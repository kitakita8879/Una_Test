package com.example.una_test.xmCamera.protocol;

import androidx.annotation.NonNull;

public class XmResult<T> {
    static <T> XmResult<T> success(@NonNull XmSuccessResult<T> result) {
        return new XmResult<>(result.result, null, result.info);
    }

    static <T> XmResult<T> fail(@NonNull XmFailResult result) {
        return new XmResult<>(result.result, result.info, null);
    }

    public final int result;
    public final String errorInfo;
    public final T info;

    private XmResult(int result, String errorInfo, T info) {
        this.result = result;
        this.errorInfo = errorInfo;
        this.info = info;
    }
}
