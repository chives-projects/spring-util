package com.csc.spring.autoconfigure.request.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.core.Ordered;

/**
 * @Description: API请求日志拦截器扩展接口，其实现Ordered接口，AOP切面会根据优先级顺序取优先级最高的拦截器
 * @Author: csc
 * @create: 2022/12/13
 */
public interface EagleRequest extends MethodInterceptor, Ordered {
}
