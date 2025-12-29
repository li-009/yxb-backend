package com.yxb.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 视频格式枚举
 */
@Getter
@AllArgsConstructor
public enum VideoFormatEnum {

    MP4("mp4", "video/mp4", true),
    MKV("mkv", "video/x-matroska", true),
    AVI("avi", "video/x-msvideo", true),
    FLV("flv", "video/x-flv", true),
    MOV("mov", "video/quicktime", true),
    WMV("wmv", "video/x-ms-wmv", true),
    WEBM("webm", "video/webm", true),
    M4V("m4v", "video/x-m4v", true),
    TS("ts", "video/mp2t", false),
    M3U8("m3u8", "application/x-mpegURL", false);

    /**
     * 扩展名
     */
    private final String extension;

    /**
     * MIME类型
     */
    private final String mimeType;

    /**
     * 是否支持直接播放
     */
    private final boolean directPlay;

    /**
     * 获取所有支持的扩展名
     */
    public static Set<String> getSupportedExtensions() {
        return Arrays.stream(values())
                .map(VideoFormatEnum::getExtension)
                .collect(Collectors.toSet());
    }

    /**
     * 根据扩展名获取格式
     */
    public static VideoFormatEnum getByExtension(String extension) {
        if (extension == null) {
            return null;
        }
        String ext = extension.toLowerCase().replace(".", "");
        for (VideoFormatEnum format : values()) {
            if (format.getExtension().equals(ext)) {
                return format;
            }
        }
        return null;
    }

    /**
     * 是否支持该格式
     */
    public static boolean isSupported(String extension) {
        return getByExtension(extension) != null;
    }
}
