package com.csc.spring.autoconfigure.converters;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: csc
 * @description: jackson转换器配置类
 * @create: 2022/12/13
 */
@ConfigurationProperties(prefix = "spring.csc.jackson2.converter")
public class Jackson2MessagesProperties {
    /**
     * 是否开启json转换器配置
     */
    private boolean enabled;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
