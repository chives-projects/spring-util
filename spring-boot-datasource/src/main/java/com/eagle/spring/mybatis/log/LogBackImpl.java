package com.eagle.spring.mybatis.log;

import com.eagle.logback.EagleLoggerFactory;
import org.apache.ibatis.logging.Log;
import org.slf4j.LoggerFactory;

/**
 * @Description: 将mybatis sql语句记录到日志文件中实现类，是 org.apache.ibatis.logging.stdout.StdOutImpl 类的替换
 * @Author: csc
 * @create: 2023/01/06
 */
public class LogBackImpl implements Log {
    public LogBackImpl(String clazz) {
        // Do Nothing
    }

    @Override
    public boolean isDebugEnabled() {
        return true;
    }

    @Override
    public boolean isTraceEnabled() {
        return true;
    }

    @Override
    public void error(String s, Throwable e) {
        LoggerFactory.getLogger(LogBackImpl.class).error(s);
//        EagleLoggerFactory.getModuleLogger(LogBackImpl.class, "database", "database").error(s);
        e.printStackTrace(System.err);
    }

    @Override
    public void error(String s) {
        LoggerFactory.getLogger(LogBackImpl.class).error(s);
//        EagleLoggerFactory.getModuleLogger(LogBackImpl.class, "database", "database").error(s);
    }

    @Override
    public void debug(String s) {
        LoggerFactory.getLogger(LogBackImpl.class).debug(s);
//        EagleLoggerFactory.getModuleLogger(LogBackImpl.class, "database", "database").debug(s);
    }

    @Override
    public void trace(String s) {
        LoggerFactory.getLogger(LogBackImpl.class).trace(s);
//        EagleLoggerFactory.getModuleLogger(LogBackImpl.class, "database", "database").trace(s);
    }

    @Override
    public void warn(String s) {
        LoggerFactory.getLogger(LogBackImpl.class).warn(s);
//        EagleLoggerFactory.getModuleLogger(LogBackImpl.class, "database", "database").warn(s);
    }
}
