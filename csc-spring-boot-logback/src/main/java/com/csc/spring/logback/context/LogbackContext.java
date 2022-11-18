package com.csc.spring.logback.context;

import com.csc.spring.logback.LogbackProperties;
import com.csc.spring.logback.classic.Logback;
import com.csc.spring.logback.classic.LogbackGroupImpl;
import com.csc.spring.logback.classic.LogbackModuleImpl;
import com.csc.spring.logback.classic.LogbackRootImpl;
import com.csc.spring.logback.enumeration.LogbackType;
import org.slf4j.Logger;

import java.text.MessageFormat;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 日志类 logback+slf4j
 *
 * @create: 2022/11/18
*/
public class LogbackContext {

    /**
     * Logger对象容器
     */
    private static final Map<String, Logger> CONTEXT = new ConcurrentHashMap<>();

    private LogbackProperties properties;

    public LogbackContext(LogbackProperties properties) {
        this.properties = properties;
    }

    /**
     * 初始化日志Root对象
     */
    public void init() {
        new LogbackRootImpl(properties).getLogger();
    }

    /**
     * 获取日志输出对象
     *
     * @param fileName    日志文件名|模块名称
     * @param logbackType 日志类别
     * @return
     */
    public <T> Logger getLogger(Class<T> clazz, String filePath, String fileName, LogbackType logbackType) {
        // 获取缓存key
        String appenderName = getAppenderName(filePath, fileName, logbackType);
        //获取loggerName
        String loggerName = getLoggerName(clazz, appenderName);
        // 获取Logger对象
        Logger logger = CONTEXT.get(loggerName);
        if (Objects.nonNull(logger)) {
            return logger;
        }
        synchronized (this) {
            logger = CONTEXT.get(loggerName);
            if (Objects.nonNull(logger)) {
                return logger;
            }
            //获取logger日志对象
            logger = getLogger(loggerName, appenderName, filePath, fileName, logbackType);
            //存入缓存
            CONTEXT.put(loggerName, logger);
        }
        return logger;
    }


    /**
     * 构建Logger对象
     * 日志级别以及优先级排序: OFF > ERROR > WARN > INFO > DEBUG > TRACE >ALL
     *
     * @param fileName 日志文件名|模块名称
     * @return
     */
    protected Logger getLogger(String loggerName, String appenderName, String filePath, String fileName, LogbackType logbackType) {
        Logback logback;
        if (logbackType.getType().equals(LogbackType.MODULE.getType())) {
            logback = new LogbackModuleImpl(this.properties);
        } else {
            logback = new LogbackGroupImpl(this.properties);
        }
        return logback.getLogger(loggerName, appenderName, filePath, fileName);
    }

    /**
     * 获取appenderName
     *
     * @param clazz        当前类实例
     * @param appenderName appender属性名
     * @param <T>
     * @return appenderName
     */
    private <T> String getLoggerName(Class<T> clazz, String appenderName) {
        return MessageFormat.format("{0}.{1}", appenderName, clazz.getName());
    }

    /**
     * @param filePath    路径
     * @param fileName    文件名
     * @param logbackType 类型
     * @return
     */
    private String getAppenderName(String filePath, String fileName, LogbackType logbackType) {
        return MessageFormat.format("{0}{1}.{2}", filePath, fileName, logbackType.getType()).replace("/", ".");
    }
}
