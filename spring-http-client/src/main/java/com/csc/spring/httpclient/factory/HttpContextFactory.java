package com.csc.spring.httpclient.factory;

import com.csc.spring.httpclient.context.HttpContextHolder;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.protocol.HttpContext;
import org.springframework.http.HttpMethod;

import java.net.URI;
import java.util.Objects;
import java.util.function.BiFunction;

/**
 * @Description: 自定义HttpContext HTTP进程执行状态，它是一种可用于将属性名称映射到属性值的结构
 * HttpComponentsClientHttpRequestFactory#createRequest
 * @author: csc
 * @create: 2022/12/14
 */
public class HttpContextFactory implements BiFunction<HttpMethod, URI, HttpContext> {
    @Override
    public HttpContext apply(HttpMethod httpMethod, URI uri) {
        RequestConfig requestConfig = HttpContextHolder.current();
        //如果自定义了RequestConfig，绑定到HttpContext中做个替换
        if (Objects.nonNull(requestConfig)) {
            HttpContext context = HttpClientContext.create();
            context.setAttribute(HttpClientContext.REQUEST_CONFIG, requestConfig);
            return context;
        }
        return null;
    }
}
