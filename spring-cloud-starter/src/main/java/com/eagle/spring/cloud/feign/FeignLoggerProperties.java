package com.eagle.spring.cloud.feign;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: csc
 * @Description: 拦截器属性配置类
 * @create: 2023-01-20
 */
@ConfigurationProperties(prefix = FeignLoggerProperties.PREFIX)
public class FeignLoggerProperties {
    /**
     * 属性配置前缀
     */
    public static final String PREFIX = "eagle.feign.logger";
    /**
     * 组件开关
     */
    private boolean enabled;


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
