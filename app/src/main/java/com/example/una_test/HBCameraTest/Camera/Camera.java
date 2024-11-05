package com.example.una_test.HBCameraTest.Camera;

public interface Camera {
    CameraBean connect(String ip);

    boolean disconnect();
}