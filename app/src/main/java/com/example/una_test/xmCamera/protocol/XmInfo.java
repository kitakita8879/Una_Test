package com.example.una_test.xmCamera.protocol;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class XmInfo {
    public static class XmMediaInfo {
        @NonNull
        public final String rtsp;
        @NonNull
        public final String transport;
        public final int port;
        public final String[] rtsps;
        public final int page;

        public XmMediaInfo(@NonNull String rtsp, @NonNull String transport, int port,
                           String[] rtsps, int page) {
            this.rtsp = rtsp;
            this.transport = transport;
            this.port = port;
            this.rtsps = rtsps;
            this.page = page;
        }
    }

    public static class XmFileInfo {
        @NonNull
        public final String folder;
        public final int count;
        public final List<XmFile> files;

        public XmFileInfo(@NonNull String folder, int count, List<XmFile> files) {
            this.folder = folder;
            this.count = count;
            this.files = files;
        }

        public static class XmFile {
            @NonNull
            public final String name;
            public final int duration;
            public final long size;
            @SerializedName("createtime")
            public final int createTime;
            @Nullable
            @SerializedName("createtimestr")
            public final String createTimeStr;
            public final int type;

            public XmFile(@NonNull String name, int duration, long size, int createTime,
                          @Nullable String createTimeStr, int type) {
                this.name = name;
                this.duration = duration;
                this.size = size;
                this.createTime = createTime;
                this.createTimeStr = createTimeStr;
                this.type = type;
            }
        }
    }
}
