package com.eagle.common.utils.character;

import com.eagle.common.constant.CharacterInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author: csc
 * @description: 获取异常日志信息
 * @create: 2022/11/24
 */
public class ExceptionUtils {

    public static String getInfo(Throwable ex) {
        if (Objects.isNull(ex)) {
            return "";
        }
        //获取异常说明
        String message = ex.getMessage();
        //异常说明为空则获取异常类名x
        message = StringUtils.isEmpty(message) ? ex.getClass().getName() : message;
        //获取异常堆栈信息
        StackTraceElement[] elements = ex.getStackTrace();
        for (int i = 0; i < elements.length; i++) {
            StackTraceElement element = elements[i];
            if (i == 0) {
                message = StringUtils.join(element.toString(), " ", message);
            } else {
                message = StringUtils.join(message, CharacterInfo.ENTER, element.toString());
            }
        }
        return message;
    }
}
