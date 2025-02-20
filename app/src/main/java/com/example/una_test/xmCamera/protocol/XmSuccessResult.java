package com.example.una_test.xmCamera.protocol;

import androidx.annotation.NonNull;

public class XmSuccessResult<T> extends XmCommonResult {
    @NonNull
    public final T info;

    public XmSuccessResult(int result, @NonNull T info) {
        super(result);
        this.info = info;
    }
}
