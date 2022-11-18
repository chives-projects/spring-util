package com.csc.spring.logback.classic;

import ch.qos.logback.classic.LoggerContext;
import com.csc.spring.logback.LogbackProperties;
import org.slf4j.LoggerFactory;

/**
 * @Description: 日志实现抽象类
 *
 * @create: 2022/11/18
*/
public class AbstractLogback implements Logback {
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
}
