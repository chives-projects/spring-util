package com.csc.spring.httpclient.interceptor.timeout;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.Ordered;

/**
 * @Description: RestTemplate请求超时时间设置接口
 * @author: csc
 * @create: 2022/12/14
 */
public interface HttpTimeoutCustomizer extends MethodInterceptor, Ordered {
    /**
     * 拦截器前置方法
     *
     * @param invocation
     */
    void before(MethodInvocation invocation);

    /**
     * 拦截器后置方法
     */
    void after();
}
