package com.csc.spring.context.config.context;

import com.csc.spring.logback.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.core.Ordered;

/**
 * @Description: 上下文自动化配置类
 * @Author: csc
 * @Create: 2022/12/01
 */
@Configuration
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@EnableConfigurationProperties(ContextProperties.class)
@ConditionalOnProperty(prefix = ContextProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class ContextAutoConfiguration implements InitializingBean, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(ContextAutoConfiguration.class);

    @Autowired
    ContextProperties contextProperties;

    @Override
    public void destroy() {
        logger.info("<== 【销毁--自动化配置】----上下文配置组件【ContextAutoConfiguration】");
    }

    @Override
    public void afterPropertiesSet() {
        logger.info("==> 【初始化--自动化配置】----上下文配置组件【ContextAutoConfiguration】{}", contextProperties.getSystemNumber());
    }
}
