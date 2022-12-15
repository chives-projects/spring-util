package com.eagle.logback.classic.select;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.eagle.logback.LogbackProperties;
import com.eagle.logback.core.appender.AbstractAppender;
import com.eagle.logback.core.appender.LogbackAsyncAppender;
import com.eagle.logback.core.appender.LogbackRollingFileAppenderImpl;
import com.eagle.logback.domain.entity.LogbackAppender;
import com.eagle.logback.domain.enumeration.LevelType;
import com.eagle.logback.domain.enumeration.LogbackType;
import org.slf4j.LoggerFactory;

/**
 * @Description: 日志实现抽象类
 * @create: 2022/11/18
 */
public class AbstractLogback implements Logback {
    //获取logback的LoggerContext做自定义扩展
    private static LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

    private LogbackProperties properties;

    public AbstractLogback(LogbackProperties properties) {
        this.properties = properties;
    }


    public LoggerContext getLoggerContext() {
        return loggerContext;
    }

    public LogbackProperties getProperties() {
        return properties;
    }


    public void setLevelType(LevelType levelType) {
        this.levelType = levelType;
    }

    public LogbackType getLogbackType() {
        return logbackType;
    }

    public void setLogbackType(LogbackType logbackType) {
        this.logbackType = logbackType;
    }

    private LevelType levelType;
    private LogbackType logbackType;

    @Override
    public Logger getLogger(String loggerName, String appenderName, String filePath, String fileName) {
        // 获取logger对象
        Logger logger = this.getLoggerContext().getLogger(loggerName);
        // 设置是否向上级打印信息
        logger.setAdditive(false);
        // 设置日志级别
        logger.setLevel(Level.toLevel(levelType.levelStr));
        // 获取帮助类对象
        LogbackAppender appender = new LogbackAppender(appenderName, filePath, fileName, logbackType);
        dealAsync(logger, appender);
        return logger;
    }

    /**
     * 默认处理异步处理方法
     *
     * @param logger
     * @param appender
     */
    protected void dealAsync(Logger logger, LogbackAppender appender) {
        // appender对象
        AbstractAppender rollingFileAppender = new LogbackRollingFileAppenderImpl(this.getLoggerContext(), this.getProperties(), appender);
        // 是否开启异步日志
        if (this.getProperties().getAppender().getAsync().isEnabled()) {
            //异步appender
            LogbackAsyncAppender asyncAppender = new LogbackAsyncAppender(this.getLoggerContext(), this.getProperties());
            if (logger.getLevel().levelInt <= Level.ERROR_INT) {
                logger.addAppender(asyncAppender.getAppender(rollingFileAppender.getInstance(Level.ERROR)));
            }
            if (logger.getLevel().levelInt <= Level.WARN_INT) {
                logger.addAppender(asyncAppender.getAppender(rollingFileAppender.getInstance(Level.WARN)));
            }
            if (logger.getLevel().levelInt <= Level.INFO_INT) {
                logger.addAppender(asyncAppender.getAppender(rollingFileAppender.getInstance(Level.INFO)));
            }
            if (logger.getLevel().levelInt <= Level.DEBUG_INT) {
                logger.addAppender(asyncAppender.getAppender(rollingFileAppender.getInstance(Level.DEBUG)));
            }
            if (logger.getLevel().levelInt <= Level.TRACE_INT) {
                logger.addAppender(asyncAppender.getAppender(rollingFileAppender.getInstance(Level.TRACE)));
            }
        } else {
            if (logger.getLevel().levelInt <= Level.ERROR_INT) {
                logger.addAppender(rollingFileAppender.getInstance(Level.ERROR));
            }
            if (logger.getLevel().levelInt <= Level.WARN_INT) {
                logger.addAppender(rollingFileAppender.getInstance(Level.WARN));
            }
            if (logger.getLevel().levelInt <= Level.INFO_INT) {
                logger.addAppender(rollingFileAppender.getInstance(Level.INFO));
            }
            if (logger.getLevel().levelInt <= Level.DEBUG_INT) {
                logger.addAppender(rollingFileAppender.getInstance(Level.DEBUG));
            }
            if (logger.getLevel().levelInt <= Level.TRACE_INT) {
                logger.addAppender(rollingFileAppender.getInstance(Level.TRACE));
            }
        }
    }
}
