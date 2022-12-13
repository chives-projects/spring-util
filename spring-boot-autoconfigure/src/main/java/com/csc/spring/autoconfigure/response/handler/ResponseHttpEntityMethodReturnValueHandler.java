package com.csc.spring.autoconfigure.response.handler;

import com.csc.common.enums.ApplicationStatus;
import com.csc.common.po.BaseResponse;
import com.csc.common.utils.spring.MatchUtils;
import com.csc.common.utils.spring.RequestUtil;
import com.csc.spring.autoconfigure.response.ResponseWrapperProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author: csc
 * @Description: HttpEntity返回值控制
 * @Version: 1.0
 */
public class ResponseHttpEntityMethodReturnValueHandler implements HandlerMethodReturnValueHandler {

    private HandlerMethodReturnValueHandler proxyObject;
    private ResponseWrapperProperties returnValueProperties;

    public ResponseHttpEntityMethodReturnValueHandler(HandlerMethodReturnValueHandler proxyObject, ResponseWrapperProperties returnValueProperties) {
        this.proxyObject = proxyObject;
        this.returnValueProperties = returnValueProperties;
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return (HttpEntity.class.isAssignableFrom(returnType.getParameterType()) &&
                !RequestEntity.class.isAssignableFrom(returnType.getParameterType()));
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        //标注该请求已经在当前处理程序处理过
        mavContainer.setRequestHandled(true);
        ResponseEntity entity = (ResponseEntity) returnValue;
        //获取ResponseEntity封装的真实返回值
        Object body = (null == returnValue) ? null : entity.getBody();
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (entity.getStatusCode().value() == HttpStatus.NOT_FOUND.value()) {
            String path = ((Map) body).get("path").toString();
            BaseResponse responseData = BaseResponse.buildResponse(HttpStatus.NOT_FOUND.value(), StringUtils.join("接口【", path, "】不存在"));
            proxyObject.handleReturnValue(ResponseEntity.ok(responseData), returnType, mavContainer, webRequest);
        } else if (MatchUtils.match(returnValueProperties.getExclude(), request.getRequestURI())) {
            proxyObject.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
        } else if (null != body && (body instanceof BaseResponse)) {
            BaseResponse baseResponse = (BaseResponse) body;
            baseResponse.setSpentTime(RequestUtil.getSpentTime());
            proxyObject.handleReturnValue(baseResponse, returnType, mavContainer, webRequest);
        } else {
            //获取控制器方法返回值得泛型类型
            Type type = returnType.getMethod().getGenericReturnType();
            /**
             * 1.如果返回的是ResponseEntity类，无泛型化参数
             * 2.返回的ResponseEntity带泛型化参数，且参数是void
             */
            boolean flag = (type.equals(ResponseEntity.class)) || ((type instanceof ParameterizedType) && (((ParameterizedType) type).getActualTypeArguments()[0]).equals(Void.class));
            if (flag) {
                BaseResponse baseResponse = BaseResponse.buildResponse(ApplicationStatus.OK);
                proxyObject.handleReturnValue(ResponseEntity.ok(baseResponse), returnType, mavContainer, webRequest);
            } else {
                BaseResponse baseResponse = BaseResponse.buildResponse(ApplicationStatus.OK, body);
                proxyObject.handleReturnValue(ResponseEntity.ok(baseResponse), returnType, mavContainer, webRequest);
            }
        }
    }
}
