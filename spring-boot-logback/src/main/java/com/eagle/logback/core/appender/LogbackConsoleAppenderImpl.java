package com.eagle.logback.core.appender;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import com.eagle.logback.LogbackProperties;
import com.eagle.logback.classic.encoder.LogbackEncoder;
import com.eagle.logback.classic.filter.LogbackFilter;

/**
 * @Description: 通过名字和级别设置Appender
 * @create: 2022/11/18
*/
public class LogbackConsoleAppenderImpl extends AbstractAppender {
    /**
     * 控制台appender name
     */
    public static final String CONSOLE_NAME = "CONSOLE";

    public LogbackConsoleAppenderImpl(LoggerContext loggerContext, LogbackProperties properties) {
        super(loggerContext, properties);
    }

    /**
     * 控制台打印appender
     *
     * @param level
     * @return
     */
    @Override
    protected Appender<ILoggingEvent> getAppender(Level level) {
        //这里是可以用来设置appender的，在xml配置文件里面，是这种形式：
        ConsoleAppender<ILoggingEvent> appender = new ConsoleAppender<>();
        //设置上下文，每个logger都关联到logger上下文，默认上下文名称为default。
        // 但可以使用<contextName>设置成其他名字，用于区分不同应用程序的记录。一旦设置，不能修改。
        appender.setContext(this.getLoggerContext());
        //appender的name属性
        appender.setName(this.getAppenderName(level));
        //添加过滤器,这个level 覆盖了logging.level.com.eagle=debug中的level
        appender.addFilter(LogbackFilter.getThresholdLevelFilter(level));
        //设置编码
        appender.setEncoder(LogbackEncoder.getPatternLayoutEncoder(this.getLoggerContext(), this.getPattern()));
        //ANSI color codes支持，默认：false；请注意，基于Unix的操作系统（如Linux和Mac OS X）默认支持ANSI颜色代码。
        appender.setWithJansi(false);
        appender.start();
        return appender;

    }

    @Override
    protected String getPattern() {
        return this.getProperties().getRoot().getPatternConsole();
    }

    @Override
    protected String getAppenderName(Level level) {
        return CONSOLE_NAME;
    }
}
