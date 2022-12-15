package com.eagle.httpclient.context;

import org.apache.http.client.config.RequestConfig;
import org.springframework.core.NamedThreadLocal;

/**
 * @Description: Http进程执行状态上下文对象
 * @author: csc
 * @create: 2022/12/14
 */
public class HttpContextHolder {

    private static final ThreadLocal<RequestConfig> threadLocal = new NamedThreadLocal<>("HTTP进程执行状态上下文");

    public static void bind(RequestConfig requestConfig) {
        threadLocal.set(requestConfig);
    }

    public static RequestConfig current() {
        return threadLocal.get();
    }

    public static void unbind() {
        threadLocal.remove();
    }
}
