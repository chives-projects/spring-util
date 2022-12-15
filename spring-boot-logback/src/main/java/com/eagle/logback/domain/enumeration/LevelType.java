package com.eagle.logback.domain.enumeration;

/**
 * @Description: 日志级别 OFF > ERROR > WARN > INFO > DEBUG > TRACE >ALL
 * @create: 2022/11/18
 */
public enum LevelType {
    OFF("OFF"),
    ERROR("ERROR"),
    WARN("WARN"),
    INFO("INFO"),
    DEBUG("DEBUG"),
    TRACE("TRACE"),
    ALL("ALL");

    public String levelStr;

    LevelType(String levelStr) {
        this.levelStr = levelStr;
    }
}
