package com.csc.common.exception;

import com.csc.common.enums.BaseEnums;
import com.csc.common.i18n.LanguageCache;

/**
 * @description: 业务异常基类。所有业务异常均继承该类并实现其具体子类。由最上层统一捕获并处理对应异常
 * @Author: csc
 * @create: 2022/11/24
 * @version: 1.0
 */
public class BasicException extends RuntimeException implements BaseEnums<Integer> {
    /**
     * 错误编号
     */
    private Integer code;
    /**
     * 错误消息
     */
    private String message;
    /**
     * 是否是错误日志
     */
    private boolean isError = true;

    public BasicException() {
    }

    public BasicException(int status, String message) {
        super(message);
        this.code = status;
        this.message = LanguageCache.peek(message);
    }

    public BasicException(BaseEnums<Integer> status) {
        this(status.getCode(), status.getMessage());
    }

    public BasicException(int status, String message, boolean isError) {
        this(status, message);
        this.isError = isError;
    }

    @Override
    public Integer getCode() {
        return code;
    }
    @Override
    public String getMessage() {
        return message;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }
}
