package com.eagle.spring.autoconfigure.request.interceptor;

import com.eagle.common.constant.AttributeInfo;
import com.eagle.common.constant.CharacterInfo;
import com.eagle.common.enums.ApplicationStatus;
import com.eagle.common.exception.BasicException;
import com.eagle.common.po.BaseLogger;
import com.eagle.common.utils.character.ExceptionUtils;
import com.eagle.common.utils.character.JSONUtils;
import com.eagle.common.utils.servlet.RequestUtils;
import com.eagle.spring.core.aop.AopOrderInfo;
import com.eagle.spring.core.context.ContextHolder;
import com.eagle.spring.core.helper.RequestHelper;
import com.eagle.spring.core.helper.ThreadPoolHelper;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
            baseLogger.setRequestUrl(StringUtils.substringBefore(String.valueOf(RequestUtils.getRequest().getRequestURL()), CharacterInfo.ASK_SIGN_EN));
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
                baseLogger.setResponseBody(ExceptionUtils.getInfo(ex));
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
            RequestUtils.getRequest().setAttribute(AttributeInfo.SPENT_TIME, baseLogger.getSpentTime());
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
