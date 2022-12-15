package com.eagle.logback.core.appender;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.eagle.logback.LogbackProperties;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: Appender抽象类
 * @create: 2022/11/18
 */
public abstract class AbstractAppender {
    /**
     * Appender实例对象缓存
     */
    private static final Map<String, Appender<ILoggingEvent>> APPENDER_CACHE = new ConcurrentHashMap<>();

    /**
     * logger上下文
     */
    private LoggerContext loggerContext;
    /**
     * 属性配置
     */
    private LogbackProperties properties;

    protected AbstractAppender(LoggerContext loggerContext, LogbackProperties properties) {
        this.loggerContext = loggerContext;
        this.properties = properties;
    }

    /**
     * 获取Appender实例对象
     *
     * @param level
     * @return
     */
    public Appender<ILoggingEvent> getInstance(Level level) {
        //appender名称重新拼接
        String appenderName = this.getAppenderName(level);
        //如果已经存在，则复用;不存在生成appender存入缓存
        APPENDER_CACHE.computeIfAbsent(appenderName, key -> this.getAppender(level));
        return APPENDER_CACHE.get(appenderName);
    }

    /**
     * 获取appender对象
     *
     * @param level appender过滤日志级别
     * @return
     */
    protected abstract Appender<ILoggingEvent> getAppender(Level level);

    /**
     * 获取日志输出格式
     *
     * @return
     */
    protected abstract String getPattern();

    /**
     * 获取appenderName
     *
     * @param level 日志级别
     * @return
     */
    protected abstract String getAppenderName(Level level);

    public LoggerContext getLoggerContext() {
        return loggerContext;
    }

    public void setLoggerContext(LoggerContext loggerContext) {
        this.loggerContext = loggerContext;
    }

    public LogbackProperties getProperties() {
        return properties;
    }

    public void setProperties(LogbackProperties properties) {
        this.properties = properties;
    }
}
