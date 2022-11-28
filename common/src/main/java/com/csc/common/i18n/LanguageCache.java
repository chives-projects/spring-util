package com.csc.common.i18n;

import com.csc.common.constant.HeaderInfo;
import com.csc.common.enums.LanguageType;
import com.csc.common.utils.spring.RequestUtil;
import com.csc.common.utils.character.PinYinUtils;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;

/**
 * @Description: 异常多语言缓存
 * @Author: csc
 * @create: 2022/11/24
 */
public class LanguageCache {
    /**
     * 简体-繁体
     */
    private static Map<String, String> zhMap = Maps.newHashMap();
    /**
     * 简体-英文
     */
    private static Map<String, String> enMap = Maps.newHashMap();

    /**
     * 简体-繁体 绑定
     *
     * @param simple      简体
     * @param traditional 繁体
     */
    public static void bindZh(String simple, String traditional) {
        zhMap.put(simple, traditional);
    }

    /**
     * 简体-繁体 绑定
     */
    public static void bindZh(Map<String, String> zhCache) {
        zhMap.putAll(zhCache);
    }

    /**
     * 简体-英文 绑定
     *
     * @param simple 简体
     * @param en     英文
     */
    public static void bindEn(String simple, String en) {
        enMap.put(simple, en);
    }

    /**
     * 简体-英文 绑定
     */
    public static void bindEn(Map<String, String> enCache) {
        enMap.putAll(enCache);
    }

    /**
     * 获取简体中文对应的语言
     */
    public static String peek(String simple) {
        String language = LanguageType.ZH_HANS.getCode();
        if (RequestUtil.isServletContext()) {
            language = RequestUtil.getRequest().getHeader(HeaderInfo.LANGUAGE);
        }
        return peek(simple, language);
    }

    /**
     * 获取简体中文对应的语言
     */
    public static String peek(String simple, String language) {
        LanguageType languageType = LanguageType.getBy(language);
        return peek(simple, languageType);
    }

    /**
     * 获取简体中文对应的语言
     */
    public static String peek(String simple, LanguageType languageType) {
        if (Objects.isNull(languageType) || StringUtils.isEmpty(simple)) {
            return simple;
        }
        if (StringUtils.length(simple) > 100) {
            return simple;
        }
        if (languageType.equals(LanguageType.ZH_HANT)) {
            return zhMap.containsKey(simple) ? zhMap.get(simple) : PinYinUtils.convertToTraditionalChinese(simple);
        }
        if (languageType.equals(LanguageType.EN)) {
            return enMap.containsKey(simple) ? enMap.get(simple) : simple;
        }
        return simple;
    }

}
