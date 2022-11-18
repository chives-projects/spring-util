package com.csc.spring.logback;

import com.csc.spring.logback.context.LogbackContext;
import com.csc.spring.logback.enumeration.LogbackType;
import org.slf4j.Logger;

/**
 * @Description: 日志工厂类
 *
 * @create: 2022/11/18
*/
public class LoggerFactory {
    /**
     * Logger日志上下文
     * init getStatic put invoke new
     * reflect parentClass main
     * sync clinit
     * <p>
     * by reflect retain this class,field,method
     * <p>
     * loading -> linking -> init
     *              |
     * verify -> prepare -> resolve
     * get the binary class by complete package name
     * put the static store structure to running static pool by binary class
     * the way of visiting this class in the memory
     */
    public static LogbackContext CONTEXT;

    public static <T> Logger getLogger(Class<T> clazz) {
        return org.slf4j.LoggerFactory.getLogger(clazz);
    }

    public static <T> Logger getGroupLogger(Class<T> clazz, String filePath) {
        return getGroupLogger(clazz, filePath, null);
    }

    public static <T> Logger getGroupLogger(Class<T> clazz, String filePath, String fileName) {
        validLoggerContext();
        return CONTEXT.getLogger(clazz, filePath, fileName, LogbackType.GROUP);
    }

    public static <T> Logger getModuleLogger(Class<T> clazz, String modulePath, String fileName) {
        validLoggerContext();
        return CONTEXT.getLogger(clazz, modulePath, fileName, LogbackType.MODULE);
    }

    /**
     * 校验Logger上下文的有效性
     */
    private static void validLoggerContext() {
        if (CONTEXT == null) {
            throw new RuntimeException("logback日志上下文未初始化");
        }
    }
}
