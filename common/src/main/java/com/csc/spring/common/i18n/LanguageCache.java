package com.csc.spring.common.i18n;

import com.csc.spring.common.constant.HeaderInfo;
import com.csc.spring.common.enums.LanguageType;
import com.csc.spring.common.utils.PinYinUtils;
import com.csc.spring.common.utils.RequestUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @Description: 异常多语言缓存
 * @Author: csc
 * @create: 2022/11/24
 */
public class LanguageCache extends I18nDictionaryFactory {
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
            return getZhHantMap().containsKey(simple) ? getZhHantMap().get(simple) : PinYinUtils.convertToTraditionalChinese(simple);
        }
        if (languageType.equals(LanguageType.EN)) {
            return getEnMap().containsKey(simple) ? getEnMap().get(simple) : simple;
        }
        return simple;
    }

}
