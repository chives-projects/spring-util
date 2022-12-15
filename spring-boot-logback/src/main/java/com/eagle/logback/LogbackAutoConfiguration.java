package com.eagle.logback;

import com.eagle.common.InitializingAutoConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author: csc
 * @description: LogBack日志组件
 * @create: 2022/11/18
 */
@AutoConfiguration
@EnableConfigurationProperties(LogbackProperties.class)
@ConditionalOnProperty(prefix = LogbackProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class LogbackAutoConfiguration implements BeanFactoryPostProcessor, InitializingAutoConfig {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String[] beanName = beanFactory.getBeanNamesForType(LogbackProperties.class);
        if (beanName.length > 0 && beanFactory.containsBeanDefinition(beanName[0])) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName[0]);
            beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        }
    }
}
