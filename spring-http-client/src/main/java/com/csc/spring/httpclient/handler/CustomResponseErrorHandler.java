package com.csc.spring.httpclient.handler;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

/**
 * @author: csc
 * @description: 自定义异常处理
 * @create: 2022/12/14
 */
public class CustomResponseErrorHandler implements ResponseErrorHandler {
    /**
     * 判定响应是否有任何错误
     * true :返回的响应有错误(会去执行handleError方法)，false无错误
     *
     * @param response
     * @return
     * @throws IOException
     */
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
//        System.out.println("CustomResponseErrorHandler hasError");
        return true;
    }

    //这里拿到响应体可以做异常处理
    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
//        System.out.println("CustomResponseErrorHandler handleError");
    }
}
