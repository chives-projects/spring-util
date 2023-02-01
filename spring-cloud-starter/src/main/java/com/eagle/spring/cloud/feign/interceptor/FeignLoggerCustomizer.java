package com.eagle.spring.cloud.feign.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.core.Ordered;

/**
 * @Description: Feign日志拦截器通用接口，新增Ordered接口实现，AOP切面回去优先级最高的
 * @Author: csc
 * @create: 2023-01-20
 */
public interface FeignLoggerCustomizer extends MethodInterceptor, Ordered {
}
