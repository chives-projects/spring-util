package com.eagle.spring.core.aop;

/**
 * @author: csc
 * @Description: 定义优先级顺序
 * 过滤器-》拦截器-》切面通知
 * @create: 2022/12/13
 */
public class AopOrderInfo {
    /**
     * API请求切面的先后顺序,日志处理（包括入参和出参等）
     */
    public static final int REQUEST = 400;
    /**
     * API请求拦截器的加载顺序
     */
    public static final int REQUEST_INTERCEPTOR = 410;

    /**
     * RestTemplate请求超时设置拦截器
     */
    public static final int HTTP_CLIENT = 1000;
    /**
     * RestTemplate请求拦截器优先级
     */
    public static final int HTTP_CLIENT_INTERCEPTOR = 1100;
}
