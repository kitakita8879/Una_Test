package com.example.una_test.HBCameraTest.Camera;

public class BearCamera implements Camera {
    public BearCamera() {

    }

    @Override
    public CameraBean connect(String ip) {
        CameraBean result = new CameraBean();
        result.setErrorCode(-1);
        return result;
    }

    @Override
    public boolean disconnect() {
        return false;
    }
}
