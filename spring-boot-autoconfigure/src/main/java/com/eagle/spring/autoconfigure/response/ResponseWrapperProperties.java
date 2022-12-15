package com.eagle.spring.autoconfigure.response;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;
import java.util.Set;

/**
 * @author: csc
 * @description: 返回值配置文件类
 * @create: 2022/12/13
 */
@ConfigurationProperties(prefix = ResponseWrapperProperties.PREFIX)
public class ResponseWrapperProperties {
    /**
     * 属性配置
     */
    public static final String PREFIX = "eagle.response.wrapper";
    /**
     * 组件开关，默认：true
     */
    private boolean enabled = true;
    /**
     * 忽略包装指定URL
     */
    private Set<String> exclude = new HashSet<>();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<String> getExclude() {
        return exclude;
    }

    public void setExclude(Set<String> exclude) {
        this.exclude = exclude;
    }
}
