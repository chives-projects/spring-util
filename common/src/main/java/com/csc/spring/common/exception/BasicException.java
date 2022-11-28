package com.csc.spring.common.exception;

import com.csc.spring.common.enums.BaseEnums;
import com.csc.spring.common.i18n.LanguageCache;

/**
 * @description: 业务异常基类。所有业务异常均继承该类并实现其具体子类。由最上层统一捕获并处理对应异常
 * @Author: csc
 * @create: 2022/11/24
 * @version: 1.0
 */
public class BasicException extends RuntimeException {
    /**
     * 错误编号
     */
    private Integer statusCode;
    /**
     * 错误消息
     */
    private String errorMessage;
    /**
     * 是否是错误日志
     */
    private boolean isError = true;

    public BasicException() {
    }

    public BasicException(int status, String message) {
        super(message);
        this.statusCode = status;
        this.errorMessage = LanguageCache.peek(message);
    }

    public BasicException(BaseEnums<Integer> status) {
        this(status.getCode(), status.getMessage());
    }

    public BasicException(int status, String message, boolean isError) {
        this(status, message);
        this.isError = isError;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = LanguageCache.peek(errorMessage);
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }
}
