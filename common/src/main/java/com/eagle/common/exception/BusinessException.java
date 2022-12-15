package com.eagle.common.exception;

import com.eagle.common.enums.ApplicationStatus;
import com.eagle.common.enums.BaseEnums;

/**
 * @description: 业务异常基类。所有业务异常均继承该类并实现其具体子类。由最上层统一捕获并处理对应异常
 * @Author: csc
 * @create: 2022/11/24
 * @version: 1.0
 */
public class BusinessException extends BasicException {
    public BusinessException() {
        super(ApplicationStatus.EXCEPTION);
    }

    public BusinessException(BaseEnums<Integer> status) {
        super(status);
    }

    public BusinessException(ApplicationStatus status) {
        super(status);
    }

    public BusinessException(int status, String message) {
        super(status, message);
    }

    public BusinessException(int status, String message, boolean isError) {
        super(status, message, isError);
    }
}
