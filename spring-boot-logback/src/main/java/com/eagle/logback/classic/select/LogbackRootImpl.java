package com.eagle.logback.classic.select;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.core.util.StatusPrinter;
import com.eagle.logback.LogbackProperties;
import com.eagle.logback.core.appender.LogbackConsoleAppenderImpl;
import com.eagle.logback.domain.enumeration.LogbackType;

/**
 * @Description: 日志组件抽象类
 * @create: 2022/11/18
 */
public class LogbackRootImpl extends AbstractLogback {

    public LogbackRootImpl(LogbackProperties properties) {
        super(properties);
        setLevelType(properties.getRoot().getLevel());
        setLogbackType(LogbackType.ROOT);
    }

    /**
     * 构建RootLogger对象，需在配置类中主动调用进行初始化
     * 日志级别以及优先级排序: OFF > ERROR > WARN > INFO > DEBUG > TRACE >ALL
     */
    @Override
    public Logger getLogger() {
        LogbackProperties.Root root = this.getProperties().getRoot();
        Logger logger = super.getLogger(Logger.ROOT_LOGGER_NAME, Logger.ROOT_LOGGER_NAME, root.getFilePath(), null);

        //移除默认的console控制台appender
        logger.detachAppender(LogbackConsoleAppenderImpl.CONSOLE_NAME);
        if (this.getProperties().getRoot().isConsole()) {
            logger.addAppender(new LogbackConsoleAppenderImpl(this.getLoggerContext(), this.getProperties()).getInstance(logger.getLevel()));
        }
        //是否报告logback内部状态信息
        if (this.getProperties().getAppender().isReportState()) {
            StatusPrinter.print(this.getLoggerContext());
        }
        return logger;
    }
}
