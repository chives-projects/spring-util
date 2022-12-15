package com.eagle.spring.autoconfigure.exception.handler;

import com.eagle.common.enums.ApplicationStatus;
import com.eagle.common.exception.BasicException;
import com.eagle.common.po.BaseResponse;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.UnknownContentTypeException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * @author: csc
 * @Description: 控制并统一处理异常类
 * @ExceptionHandler标注的方法优先级问题，它会找到异常的最近继承关系，也就是继承关系最浅的注解方法
 * @Version: 1.0
 */
@RestControllerAdvice
public class DefaultGlobalExceptionHandler extends EagleGlobalExceptionRecord {
    /**
     * 运行时异常
     * 未知异常
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({Exception.class})
    public BaseResponse exceptionHandler(Exception e, HttpServletRequest request) {
        recordErrorMsg(e, request);
        return BaseResponse.buildResponse(ApplicationStatus.EXCEPTION.getCode(), e.getMessage());
    }


    /**
     * 业务异常
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(BasicException.class)
    public BaseResponse basicException(BasicException e, HttpServletRequest request) {
        recordErrorMsg(e, request);
        return BaseResponse.buildResponse(e.getCode(), e.getMessage());
    }

    /**
     * 非法代理
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({UndeclaredThrowableException.class,ResourceAccessException.class})
    public BaseResponse illegalAccessException(Exception e, HttpServletRequest request) {
        recordErrorMsg(e, request);
        return BaseResponse.buildResponse(ApplicationStatus.ILLEGAL_ACCESS);
    }

    /**
     * 空指针异常
     * 类型转换异常
     * IO异常
     * 数组越界异常
     * 数字格式异常
     * 非法计算异常
     * 不能识别的响应体类型
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({NullPointerException.class, ClassCastException.class, IOException.class, IndexOutOfBoundsException.class,
            NumberFormatException.class, ArithmeticException.class, UnknownContentTypeException.class})
    public BaseResponse illegalDataException(Exception e, HttpServletRequest request) {
        recordErrorMsg(e, request);
        return BaseResponse.buildResponse(ApplicationStatus.ILLEGAL_DATA);
    }

    /**
     * API-参数类型不匹配
     * API-缺少参数，如Get请求@RequestParam注解
     * API-控制器方法参数Validate异常
     * <p>
     * Get请求参数校验，如@NotEmpty、@NotNull等等
     * 非法参数异常捕获
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({TypeMismatchException.class, MissingRequestValueException.class, BindException.class,
            HttpMessageNotReadableException.class, ValidationException.class, IllegalArgumentException.class})
    public BaseResponse illegalArgumentException(Exception e, HttpServletRequest request) {
        recordErrorMsg(e, request);
        return BaseResponse.buildResponse(ApplicationStatus.ILLEGAL_ARGUMENT);
    }
}

