package com.eagle.spring.mybatis.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.core.Ordered;

/**
 * @Description: Mybatis埋点扩展点接口MybatisCustomizer，AOP切面会根据优先级选择优先级最高的拦截器
 * @Author: csc
 * @create: 2023/01/06
 */
public interface MybatisCustomizer extends MethodInterceptor, Ordered {
}
