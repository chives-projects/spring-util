package com.csc.common.exception;

import com.csc.common.constant.CharacterInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author: csc
 * @description: 获取打印异常日志信息
 * @create: 2022/11/24
 */
public class PrintExceptionInfo {
    /**
     * @Description 打印错误日志信息
     * @Version 1.0
     */
    public static String printErrorInfo(Throwable ex) {
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

    /**
     * 输出所有异常
     */
    public static String printErrorInfo(Throwable[] e) {
        String message = "";
        for (int i = 0; i < e.length; i++) {
            message = StringUtils.join(message, "\n", printErrorInfo(e[i]));
        }
        return message;
    }
}
