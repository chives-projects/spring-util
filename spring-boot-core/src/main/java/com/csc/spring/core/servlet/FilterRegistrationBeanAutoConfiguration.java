package com.csc.spring.core.servlet;

import com.csc.spring.logback.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import javax.servlet.Filter;
import java.util.Arrays;

/**
 * @description: 过滤器注册自动化配置,向spring容器注册filter
 */
@Configuration(proxyBeanMethods = false)
public class FilterRegistrationBeanAutoConfiguration implements InitializingBean, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(FilterRegistrationBeanAutoConfiguration.class);

    /**
     * 注册HTTP请求拦截器注册BEAN
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean<Filter> filterRegistrationBean() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setName("requestChannelFilter");
        filterRegistrationBean.setFilter(new RequestChannelFilter());
        filterRegistrationBean.setUrlPatterns(Arrays.asList("/*"));
        filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return filterRegistrationBean;
    }

    @Override
    public void destroy() {
        logger.info("<== 【销毁--自动化配置】----过滤器注册自动化配置组件【FilterRegistrationBeanAutoConfiguration】");
    }

    @Override
    public void afterPropertiesSet() {
        logger.info("==> 【初始化--自动化配置】----过滤器注册自动化配置组件【FilterRegistrationBeanAutoConfiguration】");
    }
}
