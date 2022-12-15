package com.eagle.logback.classic.select;

import ch.qos.logback.classic.Logger;
import com.eagle.logback.LogbackProperties;
import com.eagle.logback.core.appender.LogbackConsoleAppenderImpl;
import com.eagle.logback.domain.enumeration.LogbackType;

/**
 * @Description: 分组记录日志
 * @create: 2022/11/18
 */
public class LogbackGroupImpl extends AbstractLogback {

    public LogbackGroupImpl(LogbackProperties properties) {
        super(properties);
        setLevelType(properties.getGroup().getLevel());
        setLogbackType(LogbackType.GROUP);
    }

    /**
     * 构建Logger对象
     * 日志级别以及优先级排序: OFF > ERROR > WARN > INFO > DEBUG > TRACE >ALL
     * toast
     *
     * @param fileName 日志文件名|模块名称
     * @return
     */
    @Override
    public Logger getLogger(String loggerName, String appenderName, String filePath, String fileName) {
        Logger logger = super.getLogger(loggerName, appenderName, filePath, fileName);
        if (this.getProperties().getGroup().isConsole()) {
            // 添加控制台appender
            logger.addAppender(new LogbackConsoleAppenderImpl(this.getLoggerContext(), this.getProperties()).getInstance(logger.getLevel()));
        }

        return logger;
    }
}
