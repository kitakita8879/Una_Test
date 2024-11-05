package com.example.una_test.HBCameraTest;

import android.app.Application;

import com.example.una_test.HBCameraTest.Camera.BearCamera;
import com.example.una_test.HBCameraTest.Camera.Camera;
import com.example.una_test.HBCameraTest.Camera.CameraBean;
import com.example.una_test.HBCameraTest.Camera.FNCamera;
import com.example.una_test.HBCameraTest.Camera.SigmaStarCamera;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class HBCamera {
    private Camera mCamera;

    public HBCamera() {
    }

    public static void init(Application application) {
        //FNCamera 由於API_KEY 申請關係一律先註解掉
//        FNRemoteCamera.init(application);
    }

    public String connect(String ip) {
        CameraBean result = new CameraBean();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        for (CameraType type : CameraType.values()) {
            mCamera = createCamera(type);
            result = mCamera.connect(ip);
            if (result.getErrorCode() == 0) {
                return gson.toJson(result);
            }
        }
        return gson.toJson(result);
    }

    public boolean disconnect() {
        boolean isDisconnect = mCamera != null && mCamera.disconnect();
        mCamera = null;
        return isDisconnect;
    }

    private enum CameraType {
        // enum 順序會影響連線順序
        SIGMA_STAR_CAMERA, BEAR_CAMERA, FN_CAMERA
    }

    private Camera createCamera(CameraType type) {
        switch (type) {
            case FN_CAMERA:
                return new FNCamera();
            case SIGMA_STAR_CAMERA:
                return new SigmaStarCamera();
            case BEAR_CAMERA:
                return new BearCamera(); // TODO: 2024/11/5 BearCamera 目前啥都沒有
            default:
                throw new IllegalArgumentException("unknown CameraType");
        }
    }
}
