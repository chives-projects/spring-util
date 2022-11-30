package com.csc.common.exception;

import com.csc.common.enums.ApplicationStatus;
import com.csc.common.enums.BaseEnums;

/**
 * @description: 调用第三方接口方法异常
 * @Author: csc
 * @create: 2022/11/24
 */
public class RemoteInvokeException extends BasicException {
    public RemoteInvokeException() {
        super(ApplicationStatus.EXCEPTION);
    }

    public RemoteInvokeException(BaseEnums<Integer> status) {
        super(status);
    }

    public RemoteInvokeException(int status, String message) {
        super(status, message);
    }

    public RemoteInvokeException(int status, String message, boolean isError) {
        super(status, message, isError);
    }
}
