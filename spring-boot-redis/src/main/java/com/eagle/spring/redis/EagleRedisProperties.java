package com.eagle.spring.redis;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: Redis多数据源配置文件
 * @author: csc
 * @Create: 2022-12-06
 */
@ConfigurationProperties(prefix = EagleRedisProperties.PREFIX)
public class EagleRedisProperties {
    /**
     * 前缀
     */
    public static final String PREFIX = "eagle.redis";
    /**
     * 是否开启数据源组件, 默认：true
     */
    private boolean enabled = true;
    /**
     * 是否开启共享连接校验，默认：false
     * 如果校验失败，则会将之前连接关闭，创建新的连接
     * todo:针对LettuceConnectionFactory的配置，
     */
    private boolean validateConnection = false;
    /**
     * 是否开启共享本地物理连接，默认：true
     * 如果设置为false,则每一个LettuceConnection链接操作都要打开和关闭一个socket
     */
    private boolean shareNativeConnection = true;
    /**
     * 多数据源配置
     */
    private Map<String, RedisProperties> config = new HashMap<>();

    public Map<String, RedisProperties> getConfig() {
        return config;
    }

    public void setConfig(Map<String, RedisProperties> config) {
        this.config = config;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isValidateConnection() {
        return validateConnection;
    }

    public void setValidateConnection(boolean validateConnection) {
        this.validateConnection = validateConnection;
    }

    public boolean isShareNativeConnection() {
        return shareNativeConnection;
    }

    public void setShareNativeConnection(boolean shareNativeConnection) {
        this.shareNativeConnection = shareNativeConnection;
    }
}
