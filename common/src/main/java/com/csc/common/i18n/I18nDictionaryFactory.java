package com.csc.common.i18n;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @description: 国际化翻译，数据字典
 * @Author: csc
 * @create: 2022/11/24
 * @version: 1.0
 */
public class I18nDictionaryFactory {
    /**
     * 中文简体
     */
    private static final Map<String, String> zhHansMap = Maps.newHashMap();
    /**
     * 中文繁体
     */
    private static final Map<String, String> zhHantMap = Maps.newHashMap();
    /**
     * 英文
     */
    private static final Map<String, String> enMap = Maps.newHashMap();

    /**
     * 中文简体
     */
    public static void initZhHansDictionary(Map<String, String> dic) {
        zhHansMap.putAll(dic);
    }

    /**
     * 中文繁体
     */
    public static void initZhHantDictionary(Map<String, String> dic) {
        zhHantMap.putAll(dic);
    }

    /**
     * 英文
     */
    public static void initEnDictionary(Map<String, String> dic) {
        enMap.putAll(dic);
    }

    /**
     * 获取简体中文
     */
    public static Map<String, String> getZhHansMap() {
        return zhHansMap;
    }

    /**
     * 获取繁体中文
     */
    public static Map<String, String> getZhHantMap() {
        return zhHantMap;
    }

    /**
     * 获取英文
     */
    public static Map<String, String> getEnMap() {
        return enMap;
    }
}
