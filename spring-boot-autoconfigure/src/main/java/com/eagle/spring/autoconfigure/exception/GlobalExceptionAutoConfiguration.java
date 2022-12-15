package com.eagle.spring.autoconfigure.exception;

import com.eagle.common.InitializingAutoConfig;
import com.eagle.spring.autoconfigure.exception.handler.DefaultGlobalExceptionHandler;
import com.eagle.spring.autoconfigure.exception.handler.EagleGlobalExceptionRecord;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;

/**
 * @author: csc
 * @description: 异常捕获自动化配置类
 * @create: 2022/12/13
 */
@AutoConfiguration
@EnableConfigurationProperties(GlobalExceptionProperties.class)
@ConditionalOnProperty(prefix = GlobalExceptionProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class GlobalExceptionAutoConfiguration implements InitializingAutoConfig {

    /**
     * 异常抛出拦截bean初始化
     *
     * @return
     */
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @ConditionalOnMissingBean(EagleGlobalExceptionRecord.class)
    public DefaultGlobalExceptionHandler defaultGlobalExceptionHandler() {
        return new DefaultGlobalExceptionHandler();
    }
}
