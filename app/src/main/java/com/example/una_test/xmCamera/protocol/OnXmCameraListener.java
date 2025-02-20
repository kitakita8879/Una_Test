package com.example.una_test.xmCamera.protocol;

public interface OnXmCameraListener {
    void onNotification(String msg);

    void onThrowException(XmException e);
}
