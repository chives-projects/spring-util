package com.csc.common.enums;

/**
 * @description: 语言类别
 * @Author: csc
 * @create: 2022/11/24
 * @version: 1.0
 */
public enum LanguageType implements BaseEnums<String> {
    ZH_HANS("zh-hans", "中文简体"),
    ZH_HANT("zh-hant", "中文繁体"),
    EN("en", "英语");
    private final String code;
    private final String message;

    LanguageType(String code, String description) {
        this.code = code;
        this.message = description;
    }

    public static LanguageType getBy(String code) {
        code = code == null ? "zh-hans" : code;
        code = code.toLowerCase();
        LanguageType result = LanguageType.ZH_HANS;
        switch (code) {
            case "zh-hans":
                result = LanguageType.ZH_HANS;
                break;
            case "zh-hant":
                result = LanguageType.ZH_HANT;
                break;
            case "en":
                result = LanguageType.EN;
                break;
            default:
                break;
        }
        return result;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
