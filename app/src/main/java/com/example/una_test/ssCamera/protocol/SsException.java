package com.example.una_test.ssCamera.protocol;

import androidx.annotation.NonNull;

public class SsException extends Exception {
    public final SsErrorType errorType;

    public SsException(SsErrorType errorType) {
        super(errorType.msg);
        this.errorType = errorType;
    }

    public enum SsErrorType {
        SS_NOT_SUPPORT("not support"),
        SS_TIMEOUT("timeout"),
        SS_RECEIVED_ERROR("received command decode error"),
        SS_SEND_ERROR("send command decode error");

        @NonNull
        public final String msg;

        SsErrorType(@NonNull String msg) {
            this.msg = msg;
        }
    }
}
