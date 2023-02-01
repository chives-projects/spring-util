package com.eagle.spring.cloud.feign.loadbalancer;

import com.eagle.common.po.BaseLogger;
import com.eagle.spring.cloud.feign.context.FeignContextHolder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;

import java.util.Objects;

/**
 * @Author: csc
 * @Description: 定义loadbalancer执行前后可以执行的操作，此处用来获取实际的请求地址
 * @create: 2023-01-20
 */
public class FeignLoggerLoadBalancerLifecycle implements LoadBalancerLifecycle<RequestDataContext, ResponseData, ServiceInstance> {

    @Override
    public boolean supports(Class requestContextClass, Class responseClass, Class serverTypeClass) {
        return ServiceInstance.class.isAssignableFrom(serverTypeClass);
    }

    @Override
    public void onStart(Request<RequestDataContext> request) {
    }

    @Override
    public void onStartRequest(Request<RequestDataContext> request, Response<ServiceInstance> lbResponse) {
    }

    @Override
    public void onComplete(CompletionContext<ResponseData, ServiceInstance, RequestDataContext> context) {
        if (Objects.nonNull(FeignContextHolder.current())) {
            //封装异步日志信息
            BaseLogger baseLogger = FeignContextHolder.current();
            //设置请求URL
            baseLogger.setRequestUrl(context.getClientResponse().getRequestData().getUrl().toString());
        }
    }
}
