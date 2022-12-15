package com.csc.spring.core.helper;

import com.csc.common.constant.AttributeInfo;
import com.csc.common.constant.CharacterInfo;
import com.csc.common.constant.CharsetInfo;
import com.csc.common.constant.HeaderInfo;
import com.csc.common.exception.PrintExceptionInfo;
import com.csc.common.utils.character.JSONUtils;
import com.csc.common.utils.reflect.ParamNameUtils;
import com.csc.common.utils.spring.RequestUtil;
import com.csc.spring.core.context.ContextHolder;
import com.csc.spring.core.servlet.DecoratorRequestWrapper;
import com.csc.spring.logback.LoggerFactory;
import com.google.common.collect.Maps;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.core.io.InputStreamSource;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @description: http请求服务类
 * @author: csc
 * @Create: 2022/12/01
 */
public class RequestHelper {

    private static final Logger logger = LoggerFactory.getLogger(RequestHelper.class);

    /**
     * 获取请求入参,给API请求控制器获取入参
     *
     * @return
     */
    public static Map<String, Object> getApiArgs(MethodInvocation invocation) {
        if (RequestUtil.isServletContext()) {
            return getArgs(invocation, RequestUtil.getRequest());
        }
        return Collections.emptyMap();
    }

    /**
     * 获取请求入参,给API请求控制器获取入参
     *
     * @return
     */
    public static Map<String, Object> getApiArgs(HttpServletRequest request) {
        if (RequestUtil.isServletContext()) {
            return getArgs(null, request);
        }
        return Collections.emptyMap();
    }

    /**
     * 获取请求入参
     *
     * @param request
     * @return
     */
    private static Map<String, Object> getArgs(MethodInvocation invocation, HttpServletRequest request) {
        Map<String, Object> dataMap = new LinkedHashMap<>();
        try {
            Map<String, Object> paramMap = new LinkedHashMap<>();
            if (Objects.isNull(invocation)) {
                DecoratorRequestWrapper requestWrapper;
                if (request instanceof DecoratorRequestWrapper) {
                    requestWrapper = (DecoratorRequestWrapper) request;
                } else {
                    // 解决像shiro|security等拦截器链获取参数问题
                    requestWrapper = new DecoratorRequestWrapper(request);
                    requestWrapper.getInputStream();
                }
                paramMap.putAll(getParameterMap(requestWrapper.getRequestBody()));
            } else {
                paramMap.putAll(getMethodArgs(invocation));
            }
            // Get请求参数
            Enumeration<String> names = request.getParameterNames();
            while (names.hasMoreElements()) {
                String key = names.nextElement();
                if (!paramMap.containsKey(key)) {
                    paramMap.put(key, request.getParameter(key));
                }
            }

            Map<String, Object> headers = getHeaders(request);

            // 请求头
            dataMap.put(AttributeInfo.HEADERS, headers);
            // 请求参数
            dataMap.put(AttributeInfo.PARAMS, paramMap);
        } catch (Exception e) {
            logger.error("解析入参异常：" + PrintExceptionInfo.printErrorInfo(e));
        }

        return dataMap;
    }

    /**
     * 获取请求头
     *
     * @param request
     * @return
     */
    public static Map<String, Object> getHeaders(HttpServletRequest request) {
        Map<String, Object> headers = new LinkedHashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        Optional.ofNullable(headerNames).ifPresent(headerName -> {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                String value = request.getHeader(name);
                headers.put(name, value);
            }
        });

        //设置事务标识
        if (Objects.isNull(headers.get(HeaderInfo.TRACE_ID.toLowerCase()))) {
            headers.put(HeaderInfo.TRACE_ID, ContextHolder.current().getTraceId());
        }
        //设置登录号
        if (Objects.isNull(headers.get(HeaderInfo.ACCOUNT_CODE.toLowerCase()))) {
            headers.put(HeaderInfo.ACCOUNT_CODE, ContextHolder.current().getAccountCode());
        }
        return headers;
    }

    /**
     * 获取参数对象
     *
     * @param params
     * @return
     */
    public static Map<String, Object> getParameterMap(byte[] params) throws IOException {
        try {
            return JSONUtils.toObject(params, Map.class);
        } catch (Exception e) {
            return strToMap(IOUtils.toString(params, CharsetInfo.UTF8));
        }
    }

    /**
     * 获取返回结果对象
     *
     * @param body 返回结果字节数组
     * @return
     */
    public static Object getResponseBody(byte[] body) throws IOException {
        try {
            return JSONUtils.toObject(body, Object.class);
        } catch (Exception e) {
            return IOUtils.toString(body, CharsetInfo.UTF8);
        }
    }

    /**
     * 将参数转换为Map类型
     *
     * @param param
     * @return
     */
    public static Map<String, Object> strToMap(String param) {
        if (StringUtils.isEmpty(param)) {
            return Collections.emptyMap();
        }
        Map<String, Object> pMap = Maps.newLinkedHashMap();
        String[] pArray = StringUtils.split(param, CharacterInfo.AND_SIGN);
        for (int i = 0; i < pArray.length; i++) {
            String[] array = StringUtils.split(pArray[i], CharacterInfo.EQUAL_SIGN);
            if (array.length == 2) {
                pMap.put(array[0], array[1]);
            }
        }
        if (pMap.size() == 0) {
            pMap.put(AttributeInfo.PARAMS, toObject(param));
        }
        return pMap;
    }

    /**
     * 将参数转为对象
     *
     * @param param
     * @return
     */
    private static Object toObject(String param) {
        Assert.notNull(param, "参数不可为空");
        if (param.startsWith(CharacterInfo.LEFT_SQ)) {
            return JSONUtils.toJavaBean(param, List.class);
        }
        return param;
    }

    /**
     * 获取方法参数
     */
    public static Map<String, Object> getMethodArgs(MethodInvocation invocation) {
        try {
            Method method = invocation.getMethod();
            Map<String, Object> paramMap = Maps.newHashMap();
            List<String> list = ParamNameUtils.getParamNames(method);
            Object[] obj = invocation.getArguments();
            for (int i = 0; i < list.size(); i++) {
                if (isFinal(obj[i])) continue;
                paramMap.put(list.get(i), obj[i]);
            }
            return paramMap;
        } catch (Exception e) {
            logger.error(PrintExceptionInfo.printErrorInfo(e));
        }
        return Collections.emptyMap();
    }

    /**
     * 是否继续下一步
     *
     * @param value 对象值
     * @return
     */
    private static boolean isFinal(Object value) {
        if (Objects.isNull(value)) {
            return false;
        } else if (value instanceof HttpServletRequest) {
            return true;
        } else if (value instanceof HttpServletResponse) {
            return true;
        } else if (value instanceof InputStreamSource) {
            //MultipartFile是InputStreamSource的实现类
            return true;
        } else {
            return false;
        }
    }
}
