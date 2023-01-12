package com.eagle.spring.demo.controller;

import com.eagle.common.po.BaseResponse;
import com.eagle.common.utils.character.JSONUtils;
import com.eagle.httpclient.annotation.TargetHttpTimeout;
import com.eagle.httpclient.context.HttpContextHolder;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.client.config.RequestConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Description: http请求测试
 * @Author: csc
 * @Create: 2022-12-13
 * @Version: 1.0
 */
@RequestMapping("http")
@RestController
public class HttpClientController {
    @Resource
    private RestTemplate restTemplate;

    @GetMapping("get1")
    public String get1(HttpServletRequest request) {
        String timeout = request.getParameter("timeout");
        BaseResponse<String> result;
        try {
            result = restTemplate.getForObject("http://127.0.0.1:9005/api/ETFFundPromotion20220621/timeout?timeout=" + timeout, BaseResponse.class);
            System.out.println(JSONUtils.toJSONPrettyString(result));
        } finally {
            HttpContextHolder.unbind();
        }
        return timeout;
    }

    @GetMapping("targetHttpTimeoutCode")
    public String targetHttpTimeoutCode(HttpServletRequest request) {
        String timeout = request.getParameter("timeout");
        BaseResponse<String> result;
        try {
            HttpContextHolder.bind(RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(-1).build());
            result = restTemplate.getForObject("http://127.0.0.1:9005/api/ETFFundPromotion20220621/timeout?timeout=" + timeout, BaseResponse.class);
            System.out.println(JSONUtils.toJSONPrettyString(result));
        } finally {
            HttpContextHolder.unbind();
        }
        return timeout;
    }

    @GetMapping("targetHttpTimeoutAnnotation")
    @TargetHttpTimeout(readTimeout = 2000, connectTimeout = -1)
    public BaseResponse targetHttpTimeout(HttpServletRequest request) {
        String timeout = request.getParameter("timeout");
        BaseResponse<String> result = restTemplate.getForObject("http://127.0.0.1:9005/api/ETFFundPromotion20220621/timeout?timeout=" + timeout, BaseResponse.class);

        return result;
    }


    @GetMapping("timeout")
    public String timeout(HttpServletRequest request) throws IllegalArgumentException {
        String timeout = request.getParameter("timeout");
        try {
            Thread.sleep(NumberUtils.toLong(timeout, 0));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "timeout";
    }

}
