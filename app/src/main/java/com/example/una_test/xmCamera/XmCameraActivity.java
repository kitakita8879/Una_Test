package com.example.una_test.xmCamera;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.example.una_test.R;
import com.example.una_test.databinding.ActivityXmCameraBinding;
import com.example.una_test.xmCamera.protocol.OnXmCameraListener;
import com.example.una_test.xmCamera.protocol.XmCameraApi;
import com.example.una_test.xmCamera.protocol.XmException;
import com.example.una_test.xmCamera.protocol.XmInfo;
import com.example.una_test.xmCamera.protocol.XmResult;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

public class XmCameraActivity extends AppCompatActivity {
    public static final String xmCameraIp = "192.168.169.1";
    private static final String TAG = "UNA - XmCameraActivity: ";

    public String xmCameraWifiSsid = "XM_CAMERA_SSID";
    public int onlineIndex = 0, paramIndex = 0, valueIndex = 0;
    public List<String> onlineCamId = Arrays.asList("0", "1", "2", "3");
    public SpinnerAdapter camIdAdapter, paramAdapter, valueAdapter;

    private XmCameraApi mXmCameraApi;
    private ActivityXmCameraBinding mBinding;
    private final Gson mGson = new GsonBuilder().setPrettyPrinting().create();
    private final int mTimeOut = 3000;
    private int mXmCameraPort = -1;

    private static final String PARAM1 = "param 1";
    private static final String PARAM2 = "param 2";
    private static final String PARAM3 = "param 3";
    private final List<String> mParamList = List.of(PARAM1, PARAM2, PARAM3);
    private final List<String> mValueList = new ArrayList<>();
    private final Map<String, Map<String, Integer>> mParamValue =
            Map.of(PARAM1, Map.of("value 1", 1, "value 2", 2, "value 3", 3),
                    PARAM2, Map.of("value A", 11, "value B", 22, "value C", 33),
                    PARAM3, Map.of("value a", 111, "value b", 222, "value c", 333));

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_xm_camera);
        mBinding.setActivity(this);
        camIdAdapter = createSpinnerAdapter(onlineCamId);
        paramAdapter = createSpinnerAdapter(mParamList);
        valueAdapter = createSpinnerAdapter(mValueList);
        setXmCamera();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mBinding.clLayout.clearFocus();
            InputMethodManager methodManager = (InputMethodManager)
                    getSystemService(Activity.INPUT_METHOD_SERVICE);
            methodManager.hideSoftInputFromWindow(mBinding.clLayout.getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void finish() {
        connectSocket(false);
        super.finish();
    }

    public void getMediaInfo() {
        new Thread(() -> {
            try {
                XmResult<XmInfo.XmMediaInfo> result = mXmCameraApi.getMediaInfo(mTimeOut);
                mXmCameraPort = Objects.requireNonNullElse(result.info.port, -1);
                runOnUiThread(() -> showSnackBar("getMediaInfo: \n" + mGson.toJson(result)));
            } catch (XmException e) {
                runOnUiThread(() -> showSnackBar("getMediaInfo: " + e.getMessage()));
            }
        }).start();
    }

    public void setSystemTime() {
        new Thread(() -> {
            try {
                Date currentDate = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
                String systemTime = sdf.format(currentDate);
                XmResult<String> result = mXmCameraApi.setSysTime(systemTime, mTimeOut);
                runOnUiThread(() -> showSnackBar("setSystemTime " + systemTime +
                        " result is\n" + mGson.toJson(result)));
            } catch (XmException e) {
                runOnUiThread(() -> showSnackBar("setSystemTime: " + e.getMessage()));
            }
        }).start();
    }

    public void setTimeZone() {
        new Thread(() -> {
            try {
                TimeZone timeZone = TimeZone.getDefault();
                int offsetHour = (timeZone.getRawOffset() / 1000 / 60 / 60);
                XmResult<String> result = mXmCameraApi.setTimeZone(String.valueOf(offsetHour), mTimeOut);
                runOnUiThread(() -> showSnackBar("setTimeZone: " + offsetHour
                        + " result is\n" + mGson.toJson(result)));
            } catch (XmException e) {
                runOnUiThread(() -> showSnackBar("setTimeZone: " + e.getMessage()));
            }
        }).start();
    }

    public void setWifiSsid() {
        new Thread(() -> {
            try {
                XmResult<String> result = mXmCameraApi.setWifiSsid(xmCameraWifiSsid, mTimeOut);
                runOnUiThread(() -> showSnackBar("setWifiSsid: " + xmCameraWifiSsid
                        + " result is\n" + mGson.toJson(result)));
            } catch (XmException e) {
                runOnUiThread(() -> showSnackBar("setWifiSsid: " + e.getMessage()));
            }
        }).start();
    }

    public void switchCamera() {
        new Thread(() -> {
            if (onlineCamId.size() > onlineIndex) {
                try {
                    XmResult<String> result = mXmCameraApi.setParamValue("switchcam",
                            onlineCamId.get(onlineIndex), mTimeOut);
                    runOnUiThread(() -> showSnackBar("switchCamera: " + onlineCamId.get(onlineIndex)
                            + " result is\n" + mGson.toJson(result)));
                } catch (XmException e) {
                    runOnUiThread(() -> showSnackBar("switchCamera: " + e.getMessage()));
                }
            }
        }).start();
    }

    public void getFileList() {
        new Thread(() -> {
            try {
                XmResult<List<XmInfo.XmFileInfo>> result = mXmCameraApi.getFileList(mTimeOut);
                if (result.info != null) {
                    Map<String, Long> fileDateMap = new HashMap<>();
                    for (XmInfo.XmFileInfo folder : result.info) {
                        for (XmInfo.XmFileInfo.XmFile file : folder.files) {
                            fileDateMap.put(file.name, getDate(file));
                        }
                    }
                    runOnUiThread(() -> showSnackBar("getFileList: \n" + mGson.toJson(fileDateMap)));
                }
            } catch (XmException e) {
                runOnUiThread(() -> showSnackBar("getFileList: " + e.getMessage()));
            }
        }).start();
    }

    public void setParamValue() {
        new Thread(() -> {
            if (mParamList.size() > paramIndex) {
                String name = mParamList.get(paramIndex);
                String value = String.valueOf(Objects.requireNonNull(mParamValue.get(name),
                        "param " + name + " is null").get(mValueList.get(valueIndex)));
                try {
                    String result = mGson.toJson(mXmCameraApi.setParamValue(name, value, mTimeOut));
                    runOnUiThread(() -> showSnackBar(result));
                } catch (XmException e) {
                    runOnUiThread(() -> showSnackBar("setParamValue: " + e.getMessage()));
                }
            }
        }).start();
    }

    public void connectSocket(boolean connect) {
        new Thread(() -> {
            try {
                if (connect) {
                    runOnUiThread(() -> showSnackBar("connect Socket ip " + xmCameraIp
                            + " port " + mXmCameraPort));
                    mXmCameraApi.connectSocket(mXmCameraPort);
                } else {
                    mXmCameraApi.closeSocket();
                    runOnUiThread(() -> showSnackBar("disconnect Socket"));
                }
            } catch (XmException e) {
                runOnUiThread(() -> showSnackBar("connect Socket: ip " + xmCameraIp
                        + " port " + mXmCameraPort + " fail " + e.getMessage()));
            }
        }).start();
    }

    public void updateValueSpinnerItem(int position) {
        if (mParamList.size() <= position) {
            return;
        }
        mValueList.clear();
        String param = mParamList.get(position);
        mValueList.addAll(Objects.requireNonNull(mParamValue.get(param)).keySet());
        ((ArrayAdapter<?>) valueAdapter).notifyDataSetChanged();
    }

    private void setXmCamera() {
        showSnackBar("set XmCamera ip " + xmCameraIp);
        mXmCameraApi = new XmCameraApi(xmCameraIp, mListener);
    }

    private final OnXmCameraListener mListener = new OnXmCameraListener() {
        @Override
        public void onNotification(String commonMsg) {
            showSnackBar("socket receive (fake data)\n" + mGson.toJson(commonMsg));
        }

        @Override
        public void onThrowException(XmException e) {
            showSnackBar("listener socket fail " + e.getMessage());
        }
    };

    private void showSnackBar(String text) {
        Snackbar snackbar = Snackbar.make(mBinding.clLayout, text, Snackbar.LENGTH_SHORT);
        ((TextView) snackbar.getView().findViewById(
                com.google.android.material.R.id.snackbar_text)).setSingleLine(false);
        snackbar.show();
        Log.e(TAG, "showSnackBar: \n" + text);
    }

    private ArrayAdapter<String> createSpinnerAdapter(List<String> item) {
        return new ArrayAdapter<String>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, item) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(ContextCompat.getColor(this.getContext(), R.color.white));
                return v;
            }
        };
    }

    private long getDate(XmInfo.XmFileInfo.XmFile xmFile) throws XmException {
        long date = xmFile.createTime * 1000L;
        if (date == 0) {
            try {
                String dateString = xmFile.createTimeStr;
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss",
                        Locale.getDefault());
                date = Objects.requireNonNull(formatter.parse(dateString)).getTime();
            } catch (Exception e) {
                throw new XmException(XmException.XmErrorType.XM_RECEIVED_ERROR);
            }
        }
        return date;
    }
}
