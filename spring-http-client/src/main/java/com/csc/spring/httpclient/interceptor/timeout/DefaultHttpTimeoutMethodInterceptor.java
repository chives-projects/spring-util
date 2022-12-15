package com.csc.spring.httpclient.interceptor.timeout;

import com.csc.spring.httpclient.AopOrderInfo;
import com.csc.spring.httpclient.annotation.TargetHttpTimeout;
import com.csc.spring.httpclient.context.HttpContextHolder;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.http.client.config.RequestConfig;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Method;

/**
 * @Description: Http请求超时设置拦截器，即方法上标注@TargetHttpTimeout注解才会生效
 * @author: csc
 * @create: 2022/12/14
 */
public class DefaultHttpTimeoutMethodInterceptor implements HttpTimeoutCustomizer {
    /**
     * 拦截器前置方法
     *
     * @param invocation
     */
    @Override
    public void before(MethodInvocation invocation) {
        Method method = invocation.getMethod();
        if (!method.isAnnotationPresent(TargetHttpTimeout.class)) {
            return;
        }
        TargetHttpTimeout targetHttpTimeout = method.getAnnotation(TargetHttpTimeout.class);
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(targetHttpTimeout.readTimeout())
                .setConnectTimeout(targetHttpTimeout.connectTimeout())
                .build();
        HttpContextHolder.bind(requestConfig);
    }

    /**
     * 拦截器调用方法
     *
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Nullable
    @Override
    public Object invoke(@Nonnull MethodInvocation invocation) throws Throwable {
        try {
            this.before(invocation);
            return invocation.proceed();
        } finally {
            this.after();
        }
    }

    /**
     * 拦截器后置处理方法
     */
    @Override
    public void after() {
        HttpContextHolder.unbind();
    }

    @Override
    public int getOrder() {
        return AopOrderInfo.HTTP_CLIENT;
    }
}
