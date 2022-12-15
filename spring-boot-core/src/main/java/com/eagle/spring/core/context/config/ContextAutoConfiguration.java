package com.eagle.spring.core.context.config;

import com.eagle.common.InitializingAutoConfig;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Role;
import org.springframework.core.Ordered;

/**
 * @Description: 上下文自动化配置类
 * @Author: csc
 * @Create: 2022/12/01
 */
@AutoConfiguration
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@EnableConfigurationProperties(ContextProperties.class)
@ConditionalOnProperty(prefix = ContextProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class ContextAutoConfiguration implements InitializingAutoConfig {

}
