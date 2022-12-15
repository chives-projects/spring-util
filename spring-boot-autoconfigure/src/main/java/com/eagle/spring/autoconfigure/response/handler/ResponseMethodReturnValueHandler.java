package com.eagle.spring.autoconfigure.response.handler;

import com.eagle.common.enums.ApplicationStatus;
import com.eagle.common.po.BaseResponse;
import com.eagle.common.utils.servlet.MatchUtils;
import com.eagle.common.utils.servlet.RequestUtils;
import com.eagle.spring.autoconfigure.response.ResponseWrapperProperties;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: csc
 * @Description: 控制器返回返回值包装类, 处理带@ResponseBody标识的返回值类型
 * @Version: 1.0
 */
public class ResponseMethodReturnValueHandler implements HandlerMethodReturnValueHandler {

    private HandlerMethodReturnValueHandler proxyObject;
    private ResponseWrapperProperties returnValueProperties;

    public ResponseMethodReturnValueHandler(HandlerMethodReturnValueHandler proxyObject, ResponseWrapperProperties returnValueProperties) {
        this.proxyObject = proxyObject;
        this.returnValueProperties = returnValueProperties;
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return (AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), ResponseBody.class) ||
                returnType.hasMethodAnnotation(ResponseBody.class));
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        //标注该请求已经在当前处理程序处理过
        mavContainer.setRequestHandled(true);
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (MatchUtils.match(returnValueProperties.getExclude(), request.getRequestURI())) {
            proxyObject.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
        } else if (null != returnValue && (returnValue instanceof BaseResponse)) {
            BaseResponse baseResponse = (BaseResponse) returnValue;
            baseResponse.setSpentTime(RequestUtils.getSpentTime());
            proxyObject.handleReturnValue(baseResponse, returnType, mavContainer, webRequest);
        } else {
            //返回值为void类型的data字段不输出
            if (returnType.getMethod().getReturnType().equals(Void.TYPE)) {
                BaseResponse baseResponse = BaseResponse.buildResponse(ApplicationStatus.OK);
                proxyObject.handleReturnValue(baseResponse, returnType, mavContainer, webRequest);
            } else {
                BaseResponse baseResponse = BaseResponse.buildResponse(ApplicationStatus.OK, returnValue);
                proxyObject.handleReturnValue(baseResponse, returnType, mavContainer, webRequest);
            }
        }
    }

}
