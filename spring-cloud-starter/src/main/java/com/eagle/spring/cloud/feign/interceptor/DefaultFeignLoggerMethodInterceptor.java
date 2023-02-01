package com.eagle.spring.cloud.feign.interceptor;

import com.eagle.common.exception.BasicException;
import com.eagle.common.po.BaseLogger;
import com.eagle.common.utils.character.ExceptionUtils;
import com.eagle.common.utils.character.JSONUtils;
import com.eagle.spring.cloud.feign.context.FeignContextHolder;
import com.eagle.spring.core.aop.AopOrderInfo;
import com.eagle.spring.core.context.ContextHolder;
import com.eagle.spring.core.helper.RequestHelper;
import com.eagle.spring.core.helper.ThreadPoolHelper;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: csc
 * @Description: 在接口到达具体的目标即控制器方法之前获取方法的调用权限，可以在接口方法之前或者之后做Advice(增强)处理
 */
public class DefaultFeignLoggerMethodInterceptor implements FeignLoggerCustomizer {

    private static final Logger logger = LoggerFactory.getLogger(DefaultFeignLoggerMethodInterceptor.class);

    /**
     * 拦截接口日志
     *
     * @param invocation 接口方法切面连接点
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        // 开始时间
        long start = System.currentTimeMillis();
        // 响应结果
        Object response = null;
        try {
            //调用真实的action方法
            response = invocation.proceed();
            return response;
        } catch (Exception e) {
            if (e instanceof BasicException) {
                BasicException exception = (BasicException) e;
                response = StringUtils.join(e, " 【statusCode】", exception.getCode(), ", 【errorMessage】", exception.getMessage());
            } else {
                response = ExceptionUtils.getInfo(e);
            }
            throw e;
        } finally {
            //封装异步日志信息
            BaseLogger baseLogger = FeignContextHolder.current();
            //请求参数
            //todo:测试请求头
            baseLogger.setRequestParams(RequestHelper.getApiArgs(invocation));
            //客户端IP
            baseLogger.setClientIp(ContextHolder.current().getClientIp());
            //服务端IP
            baseLogger.setServerIp(ContextHolder.current().getServerIp());
            //耗时
            baseLogger.setSpentTime(System.currentTimeMillis() - start);
            //触发时间
//            baseLogger.setTriggerTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DateFormatEnum.YYYY_MM_DD_HH_MM_SS_SSS.getFormat())));
            //响应结果
            baseLogger.setResponseBody(response);
            //异步记录接口响应信息
            ThreadPoolHelper.threadPoolTaskExecutor().submit(() -> logger.info(JSONUtils.toJSONString(baseLogger)));
            //删除线程上下文中的数据，防止内存溢出
            FeignContextHolder.unbind();
            //非servlet上下文移除数据
            ContextHolder.unbind();
        }
    }

    @Override
    public int getOrder() {
        return AopOrderInfo.FEIGN_INTERCEPTOR;
    }
}
