package com.csc.spring.common.utils.cryption;

import com.csc.spring.common.constant.CharsetInfo;
import com.csc.spring.common.enums.ApplicationStatus;
import com.csc.spring.common.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @description: URL编码解码工具类 application/x-www-form-rulencoded MIME字符串之间的转换
 * just package URLDecoder,consider delete
 * @Author: csc
 * @create: 2022/11/24
 */
public class URLCoderUtils {
    /**
     * URL数据编码，默认UTF8
     *
     * @param content 数据类型
     * @return
     */
    public static String decode(String content) {
        return decode(content, CharsetInfo.UTF8);
    }

    /**
     * URL数据解码
     *
     * @param content 数据字符串
     * @param enc     编码
     * @return
     */
    public static String decode(String content, String enc) {
        if (StringUtils.isEmpty(content)) {
            return null;
        }
        try {
            return URLDecoder.decode(content, enc);
        } catch (UnsupportedEncodingException e) {
            throw new BusinessException(ApplicationStatus.ILLEGAL_ARGUMENT_EXCEPTION.getCode(), "字符串编码错误");
        }
    }

    /**
     * URL数据编码，默认UTF8
     *
     * @param content 数据类型
     * @return
     */
    public static String encode(String content) {
        return encode(content, CharsetInfo.UTF8);
    }

    /**
     * URL数据编码
     *
     * @param content 数据字符串
     * @param enc     编码
     * @return
     */
    public static String encode(String content, String enc) {
        if (StringUtils.isEmpty(content)) {
            return null;
        }
        try {
            return URLEncoder.encode(content, enc);
        } catch (UnsupportedEncodingException e) {
            throw new BusinessException(ApplicationStatus.ILLEGAL_ARGUMENT_EXCEPTION.getCode(), "字符串编码错误");
        }
    }

}
