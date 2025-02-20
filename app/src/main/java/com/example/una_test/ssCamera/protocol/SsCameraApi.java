package com.example.una_test.ssCamera.protocol;

import android.util.Log;
import android.util.Xml;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.reflect.TypeToken;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SsCameraApi {
    private static final String FAKE_CMD = "/fakeCmd/action=%s&property=%s";
    private static final String FAKE_CMD_VALUE = "/fakeCmd/action=%s&property=%s&value=%s";
    private static final String FAKE_CMD_FILE = "/fakeCmd/action=%s&property=%s&count=%s&from=%s";
    private static final String FAKE_CMD_REGEX = "/fakeCmd/action=[^&]+&property=[^&]+";
    private static final String FAKE_CMD_FILE_REGEX = "/fakeCmd/action=[^&]+&property=[^&]+&count=[^&]+&from=[^&]+";

    private final String mSsCameraIp;
    private final String mHost;
    private final XmlPullParser mParser;
    private DatagramSocket mSocket = null;
    private DatagramPacket mPacket = null;
    private Thread mSocketThread = null;
    private boolean mIsSocketClose = false;

    // TODO: 2025/2/20 fake data
    private final boolean mIsFakeData = true;

    public SsCameraApi(String ip) {
        this.mSsCameraIp = ip;
        this.mHost = "http://" + ip;
        mParser = Xml.newPullParser();
    }

    @NonNull
    public SsResult<Map<String, String>> getParamValue(@NonNull String param, @Nullable Integer timeout)
            throws SsException {
        String cmd = String.format(FAKE_CMD, "get", param);
        String result = addCmd(cmd, timeout);
        return parseCmdMapResult(result, String.class);
    }

    @NonNull
    public SsResult<List<SsInfo.SsFile>> getFileList(@NonNull String camera, @NonNull String folder,
                                                     int count, int from, @Nullable Integer timeout) throws SsException {
        String cmd = String.format(FAKE_CMD_FILE, camera, folder, count, from);
        String result = addCmd(cmd, timeout);
        return parseCmdListResult(result, SsInfo.SsFile.class);
    }

    @NonNull
    public SsResult<String> setParamValue(@NonNull String param, @NonNull String value,
                                          @Nullable Integer timeout) throws SsException {
        String cmd = String.format(FAKE_CMD_VALUE, "set", param, value);
        String result = addCmd(cmd, timeout);
        return parseCmdResult(result, String.class);
    }

    public void connectSocket() throws SsException {
        if (mSsCameraIp.isEmpty()) {
            throw new SsException(SsException.SsErrorType.SS_NOT_SUPPORT);
        }
        if (mSocket != null && mSocket.isConnected() && !mSocket.isClosed()) {
            return;
        }
        try {
            mSocket = new DatagramSocket(8080);
            mIsSocketClose = false;
        } catch (SocketException e) {
            throw new SsException(SsException.SsErrorType.SS_NOT_SUPPORT);
        }
        listenSocket();
    }

    public void closeSocket() {
        if (mSocket == null || mSocket.isClosed()) {
            return;
        }
        mSocket.close();
        if (mSocketThread != null) {
            mSocketThread.interrupt();
            mSocketThread = null;
        }
        mIsSocketClose = true;
    }

    private void listenSocket() {
        if (mSocketThread != null && mSocketThread.isAlive()) {
            return;
        }
        mSocketThread = new Thread(() -> {
            try {
                byte[] bytes = new byte[1024];
                while (!mIsSocketClose) {
                    mPacket = new DatagramPacket(bytes, bytes.length);
                    mSocket.receive(mPacket);
                    String result = new String(mPacket.getData(), 0, mPacket.getLength());
                    Log.e(this.getClass().getName(), "listenSocket: " + result);
                }
            } catch (Exception e) {
                Log.e(this.getClass().getName(), "listenSocket: " + e.getMessage());
            }
        });
        mSocketThread.start();
    }

    @NonNull
    public String addCmd(@NonNull String cmd, @Nullable Integer timeout) throws SsException {
        if (mIsFakeData) {
            return returnFakeData(cmd);
        }
        String result;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(mHost + cmd);
            Log.i(this.getClass().getName(), "addCmd: " + url);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            if (timeout != null) {
                httpURLConnection.setConnectTimeout(timeout);
                httpURLConnection.setReadTimeout(timeout);
            }
            if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new SsException(SsException.SsErrorType.SS_NOT_SUPPORT);
            }
            result = inputToString(httpURLConnection);
            Log.i(this.getClass().getName(), "receiveData: \n" + result);
        } catch (SocketTimeoutException e) {
            throw new SsException(SsException.SsErrorType.SS_TIMEOUT);
        } catch (Exception e) {
            throw new SsException(SsException.SsErrorType.SS_NOT_SUPPORT);
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return result;
    }

    @NonNull
    private String inputToString(HttpURLConnection connection) throws SsException {
        try (InputStream input = connection.getInputStream();
             ByteArrayOutputStream writer = new ByteArrayOutputStream()) {
            byte[] bytes = new byte[1024];
            int length;
            while ((length = input.read(bytes)) != -1) {
                writer.write(bytes, 0, length);
            }
            return writer.toString();
        } catch (IOException e) {
            throw new SsException(SsException.SsErrorType.SS_RECEIVED_ERROR);
        }
    }

    @SuppressWarnings("unchecked")
    @NonNull
    private <T> SsResult<Map<String, T>> parseCmdMapResult(@NonNull String cmdResult, Type type)
            throws SsException {
        TypeToken<Map<String, T>> mapToken = (TypeToken<Map<String, T>>)
                TypeToken.getParameterized(Map.class, String.class, type);
        return parseCmdResult(cmdResult, mapToken.getType());
    }

    @SuppressWarnings("unchecked")
    @NonNull
    private <T> SsResult<List<T>> parseCmdListResult(@NonNull String cmdResult, Type type)
            throws SsException {
        TypeToken<List<T>> listToken = (TypeToken<List<T>>) TypeToken.getParameterized(List.class, type);
        return parseCmdResult(cmdResult, listToken.getType());
    }

    @NonNull
    private <T> SsResult<T> parseCmdResult(@NonNull String cmdResult, Type type) throws SsException {
        if (cmdResult.matches("^(<\\?xml(?s).*?\\?>|<(?s).*?>)(?s).*?")) {
            return parseXmlResult(cmdResult, type);
        } else {
            return parseCommonResult(cmdResult, type);
        }
    }

    @SuppressWarnings("unchecked")
    @NonNull
    private <T> SsResult<T> parseXmlResult(@NonNull String cmdResult, Type type) throws SsException {
        if (!(type instanceof ParameterizedType)) {
            throw new SsException(SsException.SsErrorType.SS_RECEIVED_ERROR);
        }
        Type rawType = ((ParameterizedType) type).getRawType();
        if (rawType == List.class) {
            if (((ParameterizedType) type).getActualTypeArguments()[0] == SsInfo.SsFile.class) {
                List<SsInfo.SsFile> fileList = parseFileXml(cmdResult);
                return new SsResult<>(0, null, (T) fileList);
            }
        }
        throw new SsException(SsException.SsErrorType.SS_RECEIVED_ERROR);
    }

    private List<SsInfo.SsFile> parseFileXml(@NonNull String xmlString) throws SsException {
        List<SsInfo.SsFile> fileList = new ArrayList<>();
        try (StringReader input = new StringReader(xmlString)) {
            mParser.setInput(input);
            int eventType = mParser.getEventType();
            SsInfo.SsFile.Builder builder = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = mParser.getName();
                if (eventType == XmlPullParser.START_TAG) {
                    switch (tagName) {
                        case "name":
                            builder = new SsInfo.SsFile.Builder(mParser.nextText());
                            break;
                        case "format":
                            Objects.requireNonNull(builder).set(tagName, mParser.nextText());
                            String size = mParser.getAttributeValue(null, "size");
                            String fps = mParser.getAttributeValue(null, "fps");
                            String time = mParser.getAttributeValue(null, "time");
                            if (size != null || fps != null || time != null) {
                                builder.setFormatAttr(new SsInfo.SsFile.FormatAttr(size,
                                        fps == null ? null : Integer.parseInt(fps),
                                        time == null ? null : Integer.parseInt(time)));
                            }
                            break;
                        case "size":
                        case "attr":
                        case "time":
                            Objects.requireNonNull(builder).set(tagName, mParser.nextText());
                            break;
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if (tagName.equals("file") && builder != null) {
                        fileList.add(builder.build());
                        builder = null;
                    }
                }
                eventType = mParser.next();
            }
            return fileList;
        } catch (IOException | XmlPullParserException | RuntimeException e) {
            throw new SsException(SsException.SsErrorType.SS_RECEIVED_ERROR);
        }
    }

    @SuppressWarnings("unchecked")
    @NonNull
    private <T> SsResult<T> parseCommonResult(@NonNull String cmdResult, Type type) throws SsException {
        String[] lines = cmdResult.replaceAll("\r", "").split("\n");
        if (lines.length < 2) {
            throw new SsException(SsException.SsErrorType.SS_RECEIVED_ERROR);
        }
        int errorCode = -1;
        String errorMsg = SsException.SsErrorType.SS_NOT_SUPPORT.msg;
        int endIndex = lines.length - 1;
        for (int i = endIndex; i > 0; i--) {
            if (lines[i - 1].matches("\\d+") && !lines[i].isEmpty()) {
                errorCode = Integer.parseInt(lines[i - 1]);
                errorMsg = lines[i];
                break;
            }
        }
        if (errorCode != 0) {
            return new SsResult<>(errorCode, errorMsg, null);
        }

        if (type == String.class) {
            return new SsResult<>(errorCode, null, (T) errorMsg);
        } else if (((ParameterizedType) type).getRawType() == Map.class) {
            if (((ParameterizedType) type).getActualTypeArguments()[1] == String.class) {
                Map<String, String> valueMap = new HashMap<>();
                for (String line : lines) {
                    String[] parts = line.split("=");
                    if (parts.length > 1) {
                        valueMap.put(parts[0], parts[1]);
                    }
                }
                return new SsResult<>(errorCode, null, (T) valueMap);
            }
        }
        throw new SsException(SsException.SsErrorType.SS_RECEIVED_ERROR);
    }

    private String returnFakeData(@NonNull String cmd) {
        if (cmd.matches(FAKE_CMD_REGEX)) {
            return "Net.WIFI_STA.AP.Switch=AP\nNet.Dev.1.Type=AP\n0\nOK";
        } else if (cmd.matches(FAKE_CMD_FILE_REGEX)) {
            return "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                    "<Normal>\n<file>\n<name>/mnt/mmc/Normal/CH2/CH220230101-000003-745.mp4</name>\n" +
                    "<format>mp4</format>\n<size>20971520</size>\n<attr>RW</attr>\n" +
                    "<time>2023-01-01 00:00:04</time>\n</file>\n<file>\n" +
                    "<name>/mnt/mmc/Normal/CH2/CH220230101-000003-711.mp4</name>\n" +
                    "<format>mp4</format>\n<size>41943040</size>\n<attr>RW</attr>\n" +
                    "<time>2023-01-01 00:00:08</time>\n</file>\n<amount>2</amount>\n</Normal>";
        } else {
            return "0\nOK";
        }
    }
}
