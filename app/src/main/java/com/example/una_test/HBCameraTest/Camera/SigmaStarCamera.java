package com.example.una_test.HBCameraTest.Camera;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SigmaStarCamera implements Camera {
    private final static String TAG = "SigmaStarCamera - ";
    private final static String PATH = "/cgi-bin/Config.cgi";
    private final static int TIMEOUT = 5000;
    private HttpURLConnection mURLConnection;

    public SigmaStarCamera() {
    }

    @Override
    public CameraBean connect(String ip) {
        String cmd = PATH + "?action=get&property=Camera.Menu.SDInfo";
        CameraBean result;
        try {
            URL url = new URL("http://" + ip + cmd);
            mURLConnection = (HttpURLConnection) url.openConnection();
            mURLConnection.setConnectTimeout(TIMEOUT);
            mURLConnection.setReadTimeout(TIMEOUT);
            mURLConnection.connect();
            int responseCode = mURLConnection.getResponseCode();
            result = resultToBean(mURLConnection, responseCode);
            if (responseCode != HttpURLConnection.HTTP_OK) {
                disconnect();
            }
        } catch (Exception e) {
            disconnect();
            result = new CameraBean();
            result.setErrorCode(-1);
            Log.e(TAG, "connect: " + e.getMessage());
        }
        return result;
    }

    @Override
    public boolean disconnect() {
        if (mURLConnection != null) {
            mURLConnection.disconnect();
            return true;
        }
        return false;
    }

    private String inputToString(HttpURLConnection connection) {
        byte[] bytes = new byte[1024];
        String result = "";
        int length;
        try (InputStream input = connection.getInputStream();
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            while ((length = input.read(bytes)) != -1) {
                byteArrayOutputStream.write(bytes, 0, length);
            }
            result = byteArrayOutputStream.toString();
        } catch (IOException e) {
            Log.e(TAG, "resultToString: " + e.getMessage());
        }
        return result;
    }

    private CameraBean resultToBean(HttpURLConnection connection, int responseCode) {
        String result = inputToString(connection);
        CameraBean cameraBean = new CameraBean();
        cameraBean.setErrorCode(result.contains("0") && result.contains("OK") ? 0 : responseCode);
        if (cameraBean.getErrorCode() != 0) {
            return cameraBean;
        }
        cameraBean.setPlatform("SigmaStar");
        cameraBean.setChip("SigmaStar");
        cameraBean.setApiVer("0.0.0");
        cameraBean.setFwVer("0.0.0");
        cameraBean.setModel("SigmaStar");
        cameraBean.setBrand("SigmaStar");
        cameraBean.setRootPath("/tmp/DCIM");
        cameraBean.setSdkVer("v0.0.0");
        cameraBean.setSettinginfos(null);
        return cameraBean;
    }
}
