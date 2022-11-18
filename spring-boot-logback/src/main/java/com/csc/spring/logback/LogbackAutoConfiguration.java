package com.csc.spring.logback;

import com.csc.spring.logback.context.LogbackContext;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author csc
 * @description: LogBack日志组件
 * @create: 2022/11/18
*/
@Configuration
@EnableConfigurationProperties(LogbackProperties.class)
@ConditionalOnProperty(prefix = LogbackProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class LogbackAutoConfiguration implements BeanFactoryPostProcessor, InitializingBean, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(LogbackAutoConfiguration.class);

    /**
     * AccessLog对象
     *
     * @return
     */
    @Bean(initMethod = "init")
    public LogbackContext defaultAccessLog(LogbackProperties properties) {
        LogbackContext context = LoggerFactory.CONTEXT;
        if (context == null) {
            context = new LogbackContext(properties);
            LoggerFactory.CONTEXT = context;
        }
//        LoggerUtil.setBuilder(context);
        return context;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String[] beanName = beanFactory.getBeanNamesForType(LogbackProperties.class);
        if (beanName.length > 0 && beanFactory.containsBeanDefinition(beanName[0])) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName[0]);
            beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        }
    }

    @Override
    public void afterPropertiesSet() {
        logger.info("==> 【初始化--自动化配置】----logback日志组件初始化完成...");
    }

    @Override
    public void destroy() {
        logger.info("<== 【销毁--自动化配置】----logback日志组件销毁完成...");
    }
}
