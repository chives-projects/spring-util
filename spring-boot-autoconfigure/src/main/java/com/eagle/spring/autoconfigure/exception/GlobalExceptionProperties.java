package com.eagle.spring.autoconfigure.exception;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: csc
 * @description: 异常处理自动化配置PO
 * @create: 2022/12/13
 */
@ConfigurationProperties(prefix = GlobalExceptionProperties.PREFIX)
public class GlobalExceptionProperties {
    /**
     * 配置前缀
     */
    public static final String PREFIX = "eagle.exception";
    /**
     * 是否开启异常拦截
     */
    private boolean enabled;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
