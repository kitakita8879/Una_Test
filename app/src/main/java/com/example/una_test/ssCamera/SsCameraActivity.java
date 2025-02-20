package com.example.una_test.ssCamera;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.una_test.R;
import com.example.una_test.databinding.ActivitySsCameraBinding;
import com.example.una_test.ssCamera.protocol.SsCameraApi;
import com.example.una_test.ssCamera.protocol.SsException;
import com.example.una_test.ssCamera.protocol.SsInfo;
import com.example.una_test.ssCamera.protocol.SsResult;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SsCameraActivity extends AppCompatActivity {
    private static final String TAG = "UNA - SsCameraActivity: ";

    public final String ssCameraIp = "192.168.1.1";
    private SsCameraApi mCamera;
    private final Gson mGson = new GsonBuilder().setPrettyPrinting().create();
    private ActivitySsCameraBinding mBinding;
    private final int mTimeout = 10000;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_ss_camera);
        mBinding.setActivity(this);
        setSsCamera();
        listenUDP();
    }

    public void getCurrentParam() {
        new Thread(() -> {
            try {
                SsResult<Map<String, String>> result = mCamera.getParamValue("Net.*", mTimeout);
                runOnUiThread(() -> showSnackBar("getCurrentParam: \n" + mGson.toJson(result)));
            } catch (SsException e) {
                runOnUiThread(() -> showSnackBar("getCurrentParam: " + e.getMessage()));
            }
        }).start();
    }

    public void getFileList() {
        new Thread(() -> {
            try {
                boolean isFileEnd;
                Set<String> allCamera = Set.of("dir");
                Set<String> allFolder = Set.of("Normal");
                List<SsInfo.SsFile> result = new ArrayList<>();
                for (String camera : allCamera) {
                    for (String folder : allFolder) {
                        List<SsInfo.SsFile> foldertFileList = new ArrayList<>();
                        isFileEnd = false;
                        while (!isFileEnd) {
                            int from = foldertFileList.size();
                            List<SsInfo.SsFile> fileList = mCamera.getFileList(camera,
                                    folder, 10, from, null).info;
                            result.addAll(fileList);
                            foldertFileList.addAll(fileList);
                            isFileEnd = fileList.size() < 10;
                        }
                    }
                }
                runOnUiThread(() -> showSnackBar("getFileList: have " + result.size() + " files(fake)"));
            } catch (SsException e) {
                runOnUiThread(() -> showSnackBar("getFileList: " + e.getMessage()));
            }
        }).start();
    }

    public void setParamValue() {
        new Thread(() -> {
            try {
                SsResult<String> result = mCamera.setParamValue("param", "value", mTimeout);
                runOnUiThread(() -> showSnackBar("setParamValue: \n" + mGson.toJson(result)));
            } catch (SsException e) {
                runOnUiThread(() -> showSnackBar("setParamValue: " + e.getMessage()));
            }
        }).start();
    }

    private void setSsCamera() {
        showSnackBar("set SsCamera ip " + ssCameraIp);
        mCamera = new SsCameraApi(ssCameraIp);
    }

    private void showSnackBar(String text) {
        Snackbar snackbar = Snackbar.make(mBinding.clLayout, text, Snackbar.LENGTH_SHORT);
        ((TextView) snackbar.getView().findViewById(
                com.google.android.material.R.id.snackbar_text)).setSingleLine(false);
        snackbar.show();
        Log.e(TAG, "showSnackBar: \n" + text);
    }

    // TODO: 2024/12/18 test local UDP function
    public void sendUDP() {
        new Thread(() -> {
            try (DatagramSocket socket = new DatagramSocket()) {
                int sendPort = 8085;
                InetSocketAddress ipAddress = new InetSocketAddress("127.0.0.1", sendPort);
                socket.connect(ipAddress);
                int udpCount = 0;
                while (udpCount < 10) {
                    byte[] sendData = ("Test UDP function" + udpCount++).getBytes();
                    DatagramPacket packet = new DatagramPacket(sendData, sendData.length, ipAddress.getAddress(), sendPort);
                    socket.send(packet);
                    Thread.sleep(3000);
                }
            } catch (IOException | InterruptedException e) {
                Log.e(TAG, "sendUDP: " + e.getMessage());
            }
        }).start();
    }

    private void listenUDP() {
        new Thread(() -> {
            try (DatagramSocket socket = new DatagramSocket(8085)) {
                byte[] buffer = new byte[1024];
                while (true) {
                    DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
                    socket.receive(receivePacket);
                    String receivedData = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    showSnackBar(receivedData);
                }
            } catch (IOException e) {
                Log.e(TAG, "listenUDP: " + e.getMessage());
            }
        }).start();
    }
}
