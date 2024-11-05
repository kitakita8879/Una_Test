package com.example.una_test.HBCameraTest;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.una_test.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HBCameraTestActivity extends AppCompatActivity {

    private static final String TAG = "UNA";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hb_camera_test);
//        FNRemoteCamera fnRemoteCamera = ((ExampleApplication) getApplication()).getCamera();
        TestSigmaCamera testSigmaCamera = new TestSigmaCamera();
        HBCamera.init(getApplication());
        HBCamera hbCamera = new HBCamera();

        findViewById(R.id.txt_fn_connect).setOnClickListener(v -> {
            Toast.makeText(this, "FNCamera connect", Toast.LENGTH_SHORT).show();
//            new Thread(() -> {
//                HashMap<String, Object> response = fnRemoteCamera.connect("192.168.1.1", true);
//                Log.e(TAG, "fn connect response: " + response);
//            }).start();
        });

        findViewById(R.id.txt_fn_disconnect).setOnClickListener(v -> {
            Toast.makeText(this, "FNCamera disconnect", Toast.LENGTH_SHORT).show();
//            new Thread(() -> {
//                HashMap<String, Object> response = fnRemoteCamera.disconnect();
//                Log.e(TAG, "fn disconnect response: " + response);
//            }).start();
        });

        findViewById(R.id.txt_sig_connect).setOnClickListener(v -> new Thread(() -> {
            String response = testSigmaCamera.connect(
                    "http://192.168.1.1/cgi-bin/Config.cgi?action=get&property=Camera.Menu.SDInfo");
            Log.e(TAG, "sig connect response: " + response);
        }).start());

        findViewById(R.id.txt_sig_disconnect).setOnClickListener(v -> new Thread(() -> {
            String response = testSigmaCamera.disconnect();
            Log.e(TAG, "sig disconnect response: " + response);
        }).start());

        findViewById(R.id.txt_hb_connect).setOnClickListener(v -> new Thread(() -> {
            String response = hbCamera.connect("192.168.1.1");
            Log.e(TAG, "hb connect response: " + response);
        }).start());

        findViewById(R.id.txt_hb_disconnect).setOnClickListener(v -> new Thread(() -> {
            boolean isDisconnect = hbCamera.disconnect();
            Log.e(TAG, "hb disconnect: " + isDisconnect);
        }).start());
    }

    // TODO: 2024/11/1 sigmaStar test connect
    //  http://192.168.1.1/cgi-bin/Config.cgi?action=get&property=Camera.Menu.SDInfo
    private static class TestSigmaCamera {
        private HttpURLConnection mURLConnection;

        public TestSigmaCamera() {

        }

        public String connect(String cmd) {
            String result;
            String action = "GET";
            try {
                URL url = new URL(cmd);
                String[] params = url.getQuery().split("&");
                for (String param : params) {
                    String[] keyValue = param.split("=");
                    if (keyValue[0].equalsIgnoreCase("action")) {
                        action = keyValue[1].toUpperCase();
                    }
                }
                mURLConnection = (HttpURLConnection) url.openConnection();
                mURLConnection.setRequestMethod(action);
                mURLConnection.connect();
                int responseCode = mURLConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    result = inputToString(mURLConnection.getInputStream());
                } else {
                    result = responseCode + disconnect();
                }
            } catch (Exception e) {
                result = "error " + disconnect();
                Log.e(TAG, "connect: " + e.getMessage());
            }

            return result;
        }

        public String disconnect() {
            String response = "disconnect";
            if (mURLConnection != null) {
                mURLConnection.disconnect();
            }
            return response;
        }

        private String inputToString(InputStream inputStream) {
            byte[] bytes = new byte[1024];
            String result = "";
            int length;
            try (InputStream input = inputStream;
                 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                while ((length = input.read(bytes)) != -1) {
                    byteArrayOutputStream.write(bytes, 0, length);
                }
                result = byteArrayOutputStream.toString();
            } catch (IOException e) {
                Log.e(TAG, "inputToString: " + e.getMessage());
            }
            return result;
        }
    }
}
