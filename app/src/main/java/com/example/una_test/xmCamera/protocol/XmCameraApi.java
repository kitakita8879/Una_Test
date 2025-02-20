package com.example.una_test.xmCamera.protocol;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class XmCameraApi {
    private static final String TAG = "UNA - XmCameraApi";
    private static final String FAKE_CMD = "/fakeCmd";
    private static final String FAKE_MEDIA_CMD = "/fakeCmdMedia";
    private static final String FAKE_FILE_CMD = "/fakeCmdFile";
    private final String mXmCameraIp;
    private final String mHost;
    private final Gson mGson;
    private Socket mSocket;
    private OnXmCameraListener mListener;
    private Thread mSocketThread = null;

    // TODO: 2024/11/15 fake return
    private final boolean mIsFakeReturn = true;

    public XmCameraApi(String xmCameraIp, OnXmCameraListener listener) {
        this.mXmCameraIp = xmCameraIp;
        this.mHost = "http://" + xmCameraIp;
        mGson = new Gson();
        mListener = listener;
    }

    @NonNull
    public XmResult<XmInfo.XmMediaInfo> getMediaInfo(@Nullable Integer timeout) throws XmException {
        String result = addCmd(FAKE_MEDIA_CMD, timeout);
        return parseCmdResult(result, XmInfo.XmMediaInfo.class);
    }

    @NonNull
    public XmResult<String> setSysTime(@Nullable Integer timeout) throws XmException {
        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        String systemTime = sdf.format(currentDate);
        return setSysTime(systemTime, timeout);
    }

    @NonNull
    public XmResult<String> setSysTime(@NonNull String date, @Nullable Integer timeout) throws XmException {
        String cmd = FAKE_CMD + date;
        String result = addCmd(cmd, timeout);
        return parseCmdResult(result, String.class);
    }

    @NonNull
    public XmResult<String> setTimeZone(@Nullable Integer timeout) throws XmException {
        TimeZone timeZone = TimeZone.getDefault();
        int offsetHour = (timeZone.getRawOffset() / 1000 / 60 / 60);
        return setTimeZone(String.valueOf(offsetHour), timeout);
    }

    @NonNull
    public XmResult<String> setTimeZone(@NonNull String timezone, @Nullable Integer timeout) throws XmException {
        String cmd = FAKE_CMD + timezone;
        String result = addCmd(cmd, timeout);
        return parseCmdResult(result, String.class);
    }

    @NonNull
    public XmResult<String> setWifiSsid(@NonNull String ssid, @Nullable Integer timeout) throws XmException {
        String cmd = FAKE_CMD + ssid;
        String result = addCmd(cmd, timeout);
        return parseCmdResult(result, String.class);
    }

    @NonNull
    public XmResult<List<XmInfo.XmFileInfo>> getFileList(@Nullable Integer timeout) throws XmException {
        String result = addCmd(FAKE_FILE_CMD, timeout);
        return parseCmdListResult(result, XmInfo.XmFileInfo.class);
    }

    @NonNull
    public XmResult<String> setParamValue(@NonNull String param, @NonNull String value, @Nullable Integer timeout)
            throws XmException {
        String cmd = FAKE_CMD + param + FAKE_CMD + value;
        String result = addCmd(cmd, timeout);
        return parseCmdResult(result, String.class);
    }

    public void connectSocket(int port) throws XmException {
        if (!mIsFakeReturn) {
            if (mXmCameraIp.isEmpty() || mListener == null) {
                throw new XmException(XmException.XmErrorType.XM_NOT_SUPPORT);
            }
            if (mSocket != null && mSocket.isConnected() && !mSocket.isClosed()) {
                return;
            }
            try {
                mSocket = new Socket(mXmCameraIp, port);
            } catch (IOException e) {
                throw new XmException(XmException.XmErrorType.XM_NOT_SUPPORT);
            }
        } else {
            if (port == -1) {
                return;
            }
        }
        listenSocket();
    }

    public void closeSocket() {
        mListener = null;
        if (mSocket == null || mSocket.isClosed()) {
            return;
        }
        try {
            mSocket.close();
            if (mSocketThread != null) {
                mSocketThread.interrupt();
                mSocketThread = null;
            }
        } catch (IOException e) {
            mListener.onThrowException(new XmException(XmException.XmErrorType.XM_NOT_SUPPORT));
        }
    }

    @NonNull
    private String addCmd(@NonNull String cmd, @Nullable Integer timeout) throws XmException {
        if (mIsFakeReturn) {
            return returnFakeData(cmd);
        }
        String result;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(mHost + cmd);
            Log.e(TAG, "addCmd: \n" + url);
            urlConnection = (HttpURLConnection) url.openConnection();
            if (timeout != null) {
                urlConnection.setConnectTimeout(timeout);
                urlConnection.setReadTimeout(timeout);
            }
            if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new XmException(XmException.XmErrorType.XM_NOT_SUPPORT);
            }
            result = inputToString(urlConnection);
            Log.e(TAG, "resultString: \n" + result);
        } catch (SocketTimeoutException e) {
            throw new XmException(XmException.XmErrorType.XM_TIMEOUT);
        } catch (Exception e) {
            throw new XmException(XmException.XmErrorType.XM_NOT_SUPPORT);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return result;
    }

    @NonNull
    private String inputToString(HttpURLConnection connection) throws XmException {
        byte[] bytes = new byte[1024];
        int length;
        try (InputStream input = connection.getInputStream();
             ByteArrayOutputStream writer = new ByteArrayOutputStream()) {
            while ((length = input.read(bytes)) != -1) {
                writer.write(bytes, 0, length);
            }
            return writer.toString();
        } catch (IOException e) {
            throw new XmException(XmException.XmErrorType.XM_RECEIVED_ERROR);
        }
    }

    @NonNull
    private <T> XmResult<T> parseCmdResult(@NonNull String cmdResult, Type type) throws XmException {
        try {
            XmCommonResult commonResult = mGson.fromJson(cmdResult, XmCommonResult.class);
            if (commonResult.result != 0) {
                XmFailResult failResult = mGson.fromJson(cmdResult, new TypeToken<XmFailResult>() {
                }.getType());
                return XmResult.fail(failResult);
            } else {
                @SuppressWarnings("unchecked")
                TypeToken<XmSuccessResult<T>> typeToken = (TypeToken<XmSuccessResult<T>>)
                        TypeToken.getParameterized(XmSuccessResult.class, type);
                XmSuccessResult<T> successResult = mGson.fromJson(cmdResult, typeToken);
                return XmResult.success(successResult);
            }
        } catch (Exception e) {
            throw new XmException(XmException.XmErrorType.XM_RECEIVED_ERROR);
        }
    }

    @NonNull
    private <T> XmResult<List<T>> parseCmdListResult(@NonNull String cmdResult, Type type)
            throws XmException {
        @SuppressWarnings("unchecked")
        TypeToken<List<T>> listToken = (TypeToken<List<T>>)
                TypeToken.getParameterized(List.class, type);
        return parseCmdResult(cmdResult, listToken.getType());
    }

    private void listenSocket() {
        if (mIsFakeReturn) {
            sendNotification("{\"msgid\":\"sd\",\"info\": { \"status\": 0},\"time\": 1593422089 }");
            return;
        }
        if (mSocketThread != null && mSocketThread.isAlive()) {
            return;
        }
        mSocketThread = new Thread(() -> {
            try (InputStream input = mSocket.getInputStream();
                 ByteArrayOutputStream writer = new ByteArrayOutputStream()) {
                byte[] bytes = new byte[1024];
                int length;
                while (mSocket.isConnected() && !mSocket.isClosed()) {
                    while ((length = input.read(bytes)) != -1) {
                        writer.write(bytes, 0, length);
                        sendNotification(writer.toString());
                        writer.reset();
                    }
                    closeSocket();
                }
            } catch (SocketException e) {
                if (!mSocket.isClosed()) {
                    mListener.onThrowException(new XmException(XmException.XmErrorType.XM_RECEIVED_ERROR));
                }
            } catch (Exception e) {
                mListener.onThrowException(new XmException(XmException.XmErrorType.XM_RECEIVED_ERROR));
            }
        });
        mSocketThread.start();
    }

    private void sendNotification(String msgString) {
        Log.e(TAG, "sendNotification: " + msgString);
        mListener.onNotification(msgString);
    }

    private String returnFakeData(String cmd) {
        switch (cmd) {
            case FAKE_MEDIA_CMD:
                return "{\"result\": 0,\"info\": {\"rtsp\": \"rtsp://192.168.1.100/stream.mp4\"" +
                        ", \"transport\":\"tcp\",\"port\": 6035,\"rtsps\": [\"rtsp://192.168.1.100/stream.mp4\"" +
                        ",\"rtsp://192.168.1.100/stream1.mp4\"], \"page\": 1} }";
            case FAKE_FILE_CMD:
                return "{\"result\": 0, \"info\": [ { \"folder\": \"loop\", \"count\": 2, \"files\": [ " +
                        "{\"name\": \"loop/video/202012121859.mp4\", \"duration\": 120,\"size\":" +
                        " 200000,\"createtime\": 1593417777,\"type\": 2 }, {\"name\": " +
                        "\"loop/video/202012121859.mp4\", \"duration\": 120,\"size\": 120000000," +
                        "\"createtime\": 1593417777, \"type\": 2 } ] }, {\"folder\": \"emr\", \"count\"" +
                        ": 2, \"files\": [ {\"name\": \"emr/video/202012121839.mp4\", \"duration\": 120, " +
                        "\"size\": 120000000, \"createtime\": 1593416877, \"type\": 2 }, {\"name\":" +
                        " \"emr/video/202012121829.mp4\", \"duration\": 120,\"size\": 120000000," +
                        "\"createtime\": 1593416677, \"type\": 2 } ] }, {\"folder\": \"event\"," +
                        " \"count\": 2, \"files\": [ {\"name\": \"event/img/202012121839.jpg\"," +
                        " \"duration\": 0,\"size\": 1200,\"createtime\": 1593416877,\"type\": 1 }," +
                        " {\"name\": \"event/img/202012121829.jpg\", \"duration\": 0,\"size\": 1200," +
                        "\"createtime\": 1593416677, \"type\": 1 } ] } ] }";
            default:
                return "{\"result\":0,\"info\":\"success\"}";
        }
    }
}
