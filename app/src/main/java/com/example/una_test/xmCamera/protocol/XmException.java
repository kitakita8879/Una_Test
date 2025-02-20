package com.example.una_test.xmCamera.protocol;

import androidx.annotation.NonNull;

public class XmException extends Exception {
    public final XmErrorType errorType;

    public XmException(XmErrorType errorType) {
        super(errorType.msg);
        this.errorType = errorType;
    }

    public enum XmErrorType {
        XM_NOT_SUPPORT("not support"),
        XM_TIMEOUT("timeout"),
        XM_RECEIVED_ERROR("received command decode error");

        @NonNull
        public final String msg;

        XmErrorType(@NonNull String msg) {
            this.msg = msg;
        }
    }
}
