package com.yxb.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 字幕格式枚举
 */
@Getter
@AllArgsConstructor
public enum SubtitleFormatEnum {

    SRT("srt", "SubRip Subtitle", true),
    ASS("ass", "Advanced SubStation Alpha", true),
    SSA("ssa", "SubStation Alpha", true),
    VTT("vtt", "Web Video Text Tracks", true),
    SUB("sub", "MicroDVD Subtitle", false),
    IDX("idx", "VobSub Index", false),
    LRC("lrc", "Lyrics", false);

    /**
     * 扩展名
     */
    private final String extension;

    /**
     * 格式名称
     */
    private final String name;

    /**
     * 是否支持解析
     */
    private final boolean parseable;

    /**
     * 获取所有支持解析的扩展名
     */
    public static Set<String> getParseableExtensions() {
        return Arrays.stream(values())
                .filter(SubtitleFormatEnum::isParseable)
                .map(SubtitleFormatEnum::getExtension)
                .collect(Collectors.toSet());
    }

    /**
     * 根据扩展名获取格式
     */
    public static SubtitleFormatEnum getByExtension(String extension) {
        if (extension == null) {
            return null;
        }
        String ext = extension.toLowerCase().replace(".", "");
        for (SubtitleFormatEnum format : values()) {
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
        SubtitleFormatEnum format = getByExtension(extension);
        return format != null && format.isParseable();
    }
}
