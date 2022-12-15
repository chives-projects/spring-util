package com.csc.spring.httpclient;

/**
 * @author: csc
 * @Description: 定义优先级顺序
 * 过滤器-》拦截器-》切面通知
 * @create: 2022/12/14
 */
public class AopOrderInfo {
    /**
     * RestTemplate请求超时设置拦截器
     */
    public static final int HTTP_CLIENT = 1000;
    /**
     * RestTemplate请求拦截器优先级
     */
    public static final int HTTP_CLIENT_INTERCEPTOR = 1100;


}
