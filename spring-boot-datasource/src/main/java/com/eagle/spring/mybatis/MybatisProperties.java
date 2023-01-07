package com.eagle.spring.mybatis;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @description: 数据源配置文件
 * @Author: csc
 * @create: 2023/01/06
 */
@ConfigurationProperties(prefix = MybatisProperties.PREFIX)
public class MybatisProperties {
    /**
     * 前缀
     */
    public static final String PREFIX = "eagle.mybatis";
    /**
     * 是否开启数据源组件, 默认：true
     */
    private boolean enabled = true;
    /**
     * 是否拦截超类或者接口中的方法，默认：true
     */
    private boolean checkInherited = true;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isCheckInherited() {
        return checkInherited;
    }

    public void setCheckInherited(boolean checkInherited) {
        this.checkInherited = checkInherited;
    }
}
