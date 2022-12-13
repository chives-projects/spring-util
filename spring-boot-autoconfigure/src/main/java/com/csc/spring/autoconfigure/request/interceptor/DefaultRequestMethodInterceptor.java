package com.csc.spring.autoconfigure.request.interceptor;

import com.csc.spring.autoconfigure.AopOrderInfo;
import com.csc.common.constant.CharacterInfo;
import com.csc.common.enums.ApplicationStatus;
import com.csc.common.exception.BasicException;
import com.csc.common.exception.PrintExceptionInfo;
import com.csc.common.po.BaseLogger;
import com.csc.common.utils.character.JSONUtils;
import com.csc.common.utils.spring.RequestUtil;
import com.csc.spring.core.context.ContextHolder;
import com.csc.spring.core.helper.RequestHelper;
import com.csc.spring.core.helper.ThreadPoolHelper;
import com.csc.spring.logback.LoggerFactory;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Objects;

/**
 * @author: csc
 * @Description: 在接口到达具体的目标即控制器方法之前获取方法的调用权限，可以在接口方法之前或者之后做Advice(增强)处理
 * @Version: 1.0
 */
public class DefaultRequestMethodInterceptor implements EagleRequest {

    private static final Logger logger = LoggerFactory.getLogger(DefaultRequestMethodInterceptor.class);

    /**
     * 拦截接口日志
     *
     * @param invocation 接口方法切面连接点
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        //封装异步日志信息
        BaseLogger baseLogger = new BaseLogger();
        try {
            //系统编号
            baseLogger.setSystemNumber(ContextHolder.current().getSystemNumber());
            //事务唯一编号
            baseLogger.setTraceId(ContextHolder.current().getTraceId());
            //请求url
            baseLogger.setRequestUrl(StringUtils.substringBefore(String.valueOf(RequestUtil.getRequest().getRequestURL()), CharacterInfo.ASK_SIGN_EN));
            //请求参数
            baseLogger.setRequestParams(RequestHelper.getApiArgs(invocation));
            //调用真实的action方法
            Object response = invocation.proceed();
            if (Objects.nonNull(response) && response instanceof ResponseEntity) {
                Object responseBody = ((ResponseEntity) response).getBody();
                //404 Not Fund
                handleNotFund(response, baseLogger);
                //设置响应结果
                baseLogger.setResponseBody(responseBody);
            } else {
                //设置响应结果
                baseLogger.setResponseBody(response);
            }
            return response;
        } catch (Exception ex) {
            if (ex instanceof BasicException) {
                BasicException exception = (BasicException) ex;
                //响应码
                baseLogger.setStatus(exception.getCode());
                //响应描述
                baseLogger.setMessage(exception.getMessage());
            } else {
                //响应码
                baseLogger.setStatus(ApplicationStatus.EXCEPTION.getCode());
                //响应描述
                baseLogger.setMessage(ApplicationStatus.EXCEPTION.getMessage());
                //异常响应体
                baseLogger.setResponseBody(PrintExceptionInfo.printErrorInfo(ex));
            }
            throw ex;
        } finally {
            //客户端IP
            baseLogger.setClientIp(ContextHolder.current().getClientIp());
            //服务端IP
            baseLogger.setServerIp(ContextHolder.current().getServerIp());
            //耗时
            baseLogger.setSpentTime(System.currentTimeMillis() - ContextHolder.current().getStartTime());
             //异步记录接口响应信息
            ThreadPoolHelper.threadPoolTaskExecutor().submit(() -> logger.info(JSONUtils.toJSONString(baseLogger)));
            //移除线程上下文数据
            ContextHolder.unbind(true);
            //设置耗时
//            RequestUtil.getRequest().setAttribute(AttributeInfo.SPENT_TIME, baseLogger.getSpentTime());
        }
    }

    /**
     * 404 Not Fund接口处理
     *
     * @param result
     * @param baseLogger
     */
    private void handleNotFund(Object result, BaseLogger baseLogger) {
        int status = ((ResponseEntity) result).getStatusCodeValue();
        if (status == HttpStatus.NOT_FOUND.value()) {
            Object resultBody = ((ResponseEntity) result).getBody();
            Map<String, Object> dataMap = JSONUtils.toJavaBean(JSONUtils.toJSONString(resultBody), Map.class);
            baseLogger.setRequestUrl(dataMap.get("path").toString());
            baseLogger.setStatus(status);
            baseLogger.setMessage(dataMap.get("error").toString());
        }
    }


    @Override
    public int getOrder() {
        return AopOrderInfo.REQUEST_INTERCEPTOR;
    }
}
