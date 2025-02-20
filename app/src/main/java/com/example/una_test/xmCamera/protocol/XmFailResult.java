package com.example.una_test.xmCamera.protocol;

import androidx.annotation.Nullable;

public class XmFailResult extends XmCommonResult {
    @Nullable
    public final String info;

    public XmFailResult(int result, @Nullable String info) {
        super(result);
        this.info = info;
    }
}
