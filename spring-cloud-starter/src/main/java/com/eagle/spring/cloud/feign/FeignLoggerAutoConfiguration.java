package com.eagle.spring.cloud.feign;

import com.eagle.common.InitializingAutoConfig;
import com.eagle.spring.cloud.feign.interceptor.DefaultFeignLoggerMethodInterceptor;
import com.eagle.spring.cloud.feign.interceptor.FeignLoggerCustomizer;
import com.eagle.spring.cloud.feign.interceptor.FeignRequestInterceptor;
import com.eagle.spring.cloud.feign.loadbalancer.FeignLoggerLoadBalancerLifecycle;
import com.eagle.spring.cloud.feign.logger.FeignLogger;
import com.eagle.spring.core.aop.AopOrderInfo;
import feign.Logger;
import org.springframework.aop.Advisor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalancerDefaultMappingsProviderAutoConfiguration;
import org.springframework.cloud.commons.config.CommonsConfigAutoConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.retry.annotation.RetryConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.function.Supplier;

/**
 * @Author: csc
 * @Description: 控制器切点配置
 */
@Configuration
@EnableConfigurationProperties(FeignLoggerProperties.class)
@ConditionalOnProperty(prefix = FeignLoggerProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class FeignLoggerAutoConfiguration implements BeanFactoryPostProcessor, InitializingAutoConfig {

    /**
     * @Description 定义接口拦截器切点
     * @Version 1.0
     */
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public Advisor feignAdvisor(ObjectProvider<FeignLoggerCustomizer> feignLoggerCustomizers) {
        //限定类|方法级别的切点
        Pointcut pointcut = new AnnotationMatchingPointcut(FeignClient.class, RequestMapping.class, true);
        //切面增强类
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        advisor.setPointcut(pointcut);
        advisor.setAdvice(feignLoggerCustomizers.orderedStream().findFirst().get());
        //设置增强拦截器执行顺序
        advisor.setOrder(AopOrderInfo.FEIGN);
        return advisor;
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public DefaultFeignLoggerMethodInterceptor feignLoggerMethodInterceptor() {
        return new DefaultFeignLoggerMethodInterceptor();
    }

    /**
     * Feign 请求日志拦截
     */
    @Bean
    public FeignRequestInterceptor feignRequestInterceptor() {
        Supplier<FeignRequestInterceptor> supplier = FeignRequestInterceptor::new;
        return supplier.get();
    }

    /**
     * Feign 声明周期管理，主要获取真实URL
     */
    @Bean
    public FeignLoggerLoadBalancerLifecycle feignLogLoadBalancerLifecycle() {
        Supplier<FeignLoggerLoadBalancerLifecycle> supplier = FeignLoggerLoadBalancerLifecycle::new;
        return supplier.get();
    }

    /**
     * 自定义日志系统代理feign日志系统
     */
    @Bean
    public Logger logger() {
        Supplier<Logger> supplier = FeignLogger::new;
        return supplier.get();
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        if (beanFactory.containsBeanDefinition(RetryConfiguration.class.getName())) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(RetryConfiguration.class.getName());
            beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        }
        if (beanFactory.containsBeanDefinition(CommonsConfigAutoConfiguration.class.getName())) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(CommonsConfigAutoConfiguration.class.getName());
            beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        }
        if (beanFactory.containsBeanDefinition(LoadBalancerDefaultMappingsProviderAutoConfiguration.class.getName())) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(LoadBalancerDefaultMappingsProviderAutoConfiguration.class.getName());
            beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        }
        if (beanFactory.containsBeanDefinition("loadBalancerClientsDefaultsMappingsProvider")) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition("loadBalancerClientsDefaultsMappingsProvider");
            beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        }
        if (beanFactory.containsBeanDefinition("defaultsBindHandlerAdvisor")) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition("defaultsBindHandlerAdvisor");
            beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        }
    }
}
