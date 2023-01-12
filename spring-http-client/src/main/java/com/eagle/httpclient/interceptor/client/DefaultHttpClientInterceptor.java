package com.eagle.httpclient.interceptor.client;

import com.eagle.common.constant.HeaderInfo;
import com.eagle.common.po.BaseLogger;
import com.eagle.common.utils.character.ExceptionUtils;
import com.eagle.common.utils.character.JSONUtils;
import com.eagle.spring.core.aop.AopOrderInfo;
import com.eagle.spring.core.context.ContextHolder;
import com.eagle.spring.core.helper.RequestHelper;
import com.eagle.spring.core.helper.ThreadPoolHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;

/**
 * @author: csc
 * @description: RestTemplate拦截器
 * @create: 2022/12/14
 */
public class DefaultHttpClientInterceptor implements HttpClientCustomizer {
    private static final Logger logger = LoggerFactory.getLogger(DefaultHttpClientInterceptor.class);

    /**
     * RestTemplate拦截方法
     *
     * @param request
     * @param body
     * @param execution
     * @return
     * @throws IOException
     */
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        //设置事务标识
        request.getHeaders().set(HeaderInfo.TRACE_ID, ContextHolder.current().getTraceId());
        //创建拦截日志信息
        BaseLogger baseLogger = new BaseLogger();
        //系统编号
        baseLogger.setSystemNumber(ContextHolder.current().getSystemNumber());
        //生成事物流水号
        baseLogger.setTraceId(ContextHolder.current().getTraceId());
        //请求URL
        baseLogger.setRequestUrl(request.getURI().toString());
        //请求参数
        baseLogger.setRequestParams(RequestHelper.getParameterMap(body));
        //开始计时
        long start = System.currentTimeMillis();
        try {
            //调用接口
            ClientHttpResponse response = execution.execute(request, body);
            //响应数据
            Object responseBody = RequestHelper.getResponseBody(StreamUtils.copyToByteArray(response.getBody()));
            //响应结果
            baseLogger.setResponseBody(responseBody);

            return response;
        } catch (IOException ex) {
            //响应结果
            baseLogger.setResponseBody(ExceptionUtils.getInfo(ex));
            throw ex;
        } finally {
            //客户端IP
            baseLogger.setClientIp(ContextHolder.current().getClientIp());
            //服务端IP
            baseLogger.setServerIp(ContextHolder.current().getServerIp());
            //耗时
            baseLogger.setSpentTime(System.currentTimeMillis() - start);
            //异步线程池记录日志
            ThreadPoolHelper.threadPoolTaskExecutor().submit(() -> logger.info(JSONUtils.toJSONString(baseLogger)));
            //非servlet上下文移除数据
            ContextHolder.unbind();
        }

    }

    @Override
    public int getOrder() {
        return AopOrderInfo.HTTP_CLIENT_INTERCEPTOR;
    }
}
