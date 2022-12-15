package com.eagle.spring.core.servlet;

import com.eagle.common.InitializingAutoConfig;
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
public class FilterRegistrationBeanAutoConfiguration implements InitializingAutoConfig {
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
}
