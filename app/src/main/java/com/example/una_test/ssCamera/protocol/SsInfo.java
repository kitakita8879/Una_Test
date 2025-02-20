package com.example.una_test.ssCamera.protocol;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SsInfo {
    public static class SsFile {
        @NonNull
        public final String name;
        @NonNull
        public final String format;
        public final long size;
        @NonNull
        public final String attr;
        @NonNull
        public final String time;
        @Nullable
        public final FormatAttr formatAttr;

        private SsFile(@NonNull String name, @NonNull String format, long size, @NonNull String attr,
                       @NonNull String time, @Nullable FormatAttr formatAttr) {
            this.name = name;
            this.format = format;
            this.size = size;
            this.attr = attr;
            this.time = time;
            this.formatAttr = formatAttr;
        }

        public static class FormatAttr {
            @Nullable
            public final String size;
            @Nullable
            public final Integer fps;
            @Nullable
            public final Integer time;

            public FormatAttr(@Nullable String size, @Nullable Integer fps, @Nullable Integer time) {
                this.size = size;
                this.fps = fps;
                this.time = time;
            }
        }

        public static class Builder {
            @NonNull
            private final String name;
            @NonNull
            private String format = "";
            private long size;
            @NonNull
            private String attr = "";
            @NonNull
            private String time = "";
            @Nullable
            private FormatAttr formatAttr;

            public Builder(@NonNull String name) {
                this.name = name;
            }

            public void set(@NonNull String param, @NonNull String value) {
                switch (param) {
                    case "format":
                        this.format = value;
                        break;
                    case "size":
                        value = value.isEmpty() ? "0" : value;
                        this.size = Long.parseLong(value);
                        break;
                    case "attr":
                        this.attr = value;
                        break;
                    case "time":
                        this.time = value;
                        break;
                    default:
                        throw new IllegalArgumentException("unknown file attribute");
                }
            }

            public void setFormatAttr(@Nullable FormatAttr formatAttr) {
                this.formatAttr = formatAttr;
            }

            public SsFile build() {
                return new SsFile(name, format, size, attr, time, formatAttr);
            }
        }
    }

    private SsInfo() {
    }
}
