package com.yxb.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 语言枚举
 */
@Getter
@AllArgsConstructor
public enum LanguageEnum {

    ZH_CN("zh-CN", "简体中文", "Chinese"),
    ZH_TW("zh-TW", "繁体中文", "Chinese Traditional"),
    EN_US("en-US", "英语", "English"),
    JA_JP("ja-JP", "日语", "Japanese"),
    KO_KR("ko-KR", "韩语", "Korean"),
    FR_FR("fr-FR", "法语", "French"),
    DE_DE("de-DE", "德语", "German"),
    ES_ES("es-ES", "西班牙语", "Spanish"),
    RU_RU("ru-RU", "俄语", "Russian"),
    PT_PT("pt-PT", "葡萄牙语", "Portuguese");

    /**
     * 语言代码
     */
    private final String code;

    /**
     * 中文名称
     */
    private final String nameCn;

    /**
     * 英文名称
     */
    private final String nameEn;

    public static LanguageEnum getByCode(String code) {
        for (LanguageEnum lang : values()) {
            if (lang.getCode().equalsIgnoreCase(code)) {
                return lang;
            }
        }
        return null;
    }
}
