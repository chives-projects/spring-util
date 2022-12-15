package com.eagle.spring.core.context.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description: 上下文属性配置
 * @Author: csc
 * @Create: 2022/12/01
 */
@ConfigurationProperties(prefix = ContextProperties.PREFIX)
public class ContextProperties {
    /**
     * 前缀
     */
    public static final String PREFIX = "eagle.context";
    /**
     * 上下文配置是否开启，默认：true
     */
    private boolean enabled = true;
    /**
     * 系统编号
     */
    private String systemNumber;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getSystemNumber() {
        return systemNumber;
    }

    public void setSystemNumber(String systemNumber) {
        this.systemNumber = systemNumber;
    }
}
