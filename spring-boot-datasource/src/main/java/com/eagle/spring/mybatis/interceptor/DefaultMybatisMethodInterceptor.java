package com.eagle.spring.mybatis.interceptor;

import com.eagle.common.po.BaseLogger;
import com.eagle.common.utils.character.ExceptionUtils;
import com.eagle.common.utils.character.JSONUtils;
import com.eagle.spring.core.aop.AopOrderInfo;
import com.eagle.spring.core.context.ContextHolder;
import com.eagle.spring.core.helper.RequestHelper;
import com.eagle.spring.core.helper.ThreadPoolHelper;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

/**
 * @Description: 在接口到达具体的目标即控制器方法之前获取方法的调用权限，可以在接口方法之前或者之后做Advice(增强)处理
 * @Author: csc
 * @create: 2023/01/06
 */
public class DefaultMybatisMethodInterceptor implements MybatisCustomizer {

    private static final Logger logger = LoggerFactory.getLogger(DefaultMybatisMethodInterceptor.class);

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        //开始时间
        long start = System.currentTimeMillis();

        BaseLogger baseLogger = new BaseLogger();
        try {
            Object response = invocation.proceed();
            baseLogger.setResponseBody(response);
            return response;
        } catch (Throwable ex) {
            baseLogger.setResponseBody(ExceptionUtils.getInfo(ex));
            throw ex;
        } finally {
            baseLogger.setSystemNumber(ContextHolder.current().getSystemNumber());
            baseLogger.setTraceId(ContextHolder.current().getTraceId());
            baseLogger.setClientIp(ContextHolder.current().getClientIp());
            baseLogger.setServerIp(ContextHolder.current().getServerIp());
            baseLogger.setRequestParams(RequestHelper.getMethodArgs(invocation));
            baseLogger.setRequestUrl(MessageFormat.format("{0}.{1}", invocation.getMethod().getDeclaringClass().getCanonicalName(), invocation.getMethod().getName()));
            baseLogger.setSpentTime(System.currentTimeMillis() - start);
            //非servlet上下文移除数据
            ContextHolder.unbind();
            ThreadPoolHelper.threadPoolTaskExecutor().submit(() -> logger.info(JSONUtils.toJSONString(baseLogger)));
        }
    }

    @Override
    public int getOrder() {
        return AopOrderInfo.MYBATIS_INTERCEPTOR;
    }
}
