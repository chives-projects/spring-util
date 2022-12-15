package com.csc.spring.httpclient.interceptor.client;

import org.springframework.core.Ordered;
import org.springframework.http.client.ClientHttpRequestInterceptor;

/**
 * @Description: RestTemplate拦截器接口，新增Ordered实现，AOP切面会取优先级最高
 * @author: csc
 * @create: 2022/12/14
 */
public interface HttpClientCustomizer extends ClientHttpRequestInterceptor, Ordered {
}
