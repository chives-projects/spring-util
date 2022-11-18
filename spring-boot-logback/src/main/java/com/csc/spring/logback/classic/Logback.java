package com.csc.spring.logback.classic;

import ch.qos.logback.classic.Logger;

/**
 * @Description: logger对象基类
 *
 * @create: 2022/11/18
*/
public interface Logback {
    /**
     * 获取Root Logger对象
     *
     * @return
     */
    default Logger getLogger() {
        return null;
    }

    /**
     * 获取Logger对象
     *
     * @param appenderName
     * @param path
     * @param fileName
     * @return
     */
    default Logger getLogger(String loggerName, String appenderName, String path, String fileName) {
        return null;
    }
}
