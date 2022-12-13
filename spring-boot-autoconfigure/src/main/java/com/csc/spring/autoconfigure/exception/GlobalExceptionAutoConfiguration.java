package com.csc.spring.autoconfigure.exception;

import com.csc.common.i18n.LanguageCache;
import com.csc.spring.autoconfigure.InitializingAutoConfig;
import com.csc.spring.autoconfigure.exception.handler.DefaultGlobalExceptionHandler;
import com.csc.spring.autoconfigure.exception.handler.EagleGlobalException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;

import javax.annotation.PostConstruct;

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
     * 初始化异常多语言
     */
    @PostConstruct
    public void init() {
        LanguageCache.bindEn("网络异常，请稍后再试", "Network exception, please try again later");
        LanguageCache.bindEn("非法方法请求", "Illegal method request");
        LanguageCache.bindEn("非法参数", "Illegal parameter");
        LanguageCache.bindEn("非法数据", "invalid data");
        LanguageCache.bindEn("非法访问", "Illegal access");
        LanguageCache.bindEn("非法代理", "Illegal agency");
    }

    /**
     * 异常抛出拦截bean初始化
     *
     * @return
     */
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @ConditionalOnMissingBean(EagleGlobalException.class)
    public DefaultGlobalExceptionHandler defaultGlobalExceptionHandler() {
        return new DefaultGlobalExceptionHandler();
    }
}
