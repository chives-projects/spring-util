package com.csc.common.po;

import com.csc.common.enums.ApplicationStatus;
import com.csc.common.enums.BaseEnums;
import com.csc.common.utils.spring.RequestUtil;

import java.io.Serializable;

/**
 * @Description 返回值工具类
 * @Author: csc
 * @create: 2022/11/24
 * @Version 1.0
 */
public class BaseResponse<T> implements Serializable {
    private int status;
    private String message;
    private T data;
    private long spentTime;


    public static <T> BaseResponse<T> buildResponse(int status, String message) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(status);
        baseResponse.setMessage(message);
        baseResponse.setSpentTime(RequestUtil.getSpentTime());
        return baseResponse;
    }

    public static <T> BaseResponse<T> buildResponse(BaseEnums<Integer> applicationStatus) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(applicationStatus.getCode());
        baseResponse.setMessage(applicationStatus.getMessage());
        baseResponse.setSpentTime(RequestUtil.getSpentTime());
        return baseResponse;
    }

    public static <T> BaseResponse<T> buildResponse() {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(ApplicationStatus.OK.getCode());
        baseResponse.setMessage(ApplicationStatus.OK.getMessage());
        baseResponse.setSpentTime(RequestUtil.getSpentTime());
        return baseResponse;
    }

    public static <T> BaseResponse<T> buildResponse(T data) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(ApplicationStatus.OK.getCode());
        baseResponse.setMessage(ApplicationStatus.OK.getMessage());
        baseResponse.setData(data);
        baseResponse.setSpentTime(RequestUtil.getSpentTime());
        return baseResponse;
    }

    public static <T> BaseResponse<T> buildResponse(int status, String message, T data) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(status);
        baseResponse.setMessage(message);
        baseResponse.setData(data);
        baseResponse.setSpentTime(RequestUtil.getSpentTime());
        return baseResponse;
    }

    public static <T> BaseResponse<T> buildResponse(ApplicationStatus applicationStatus, T data) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(applicationStatus.getCode());
        baseResponse.setMessage(applicationStatus.getMessage());
        baseResponse.setData(data);
        baseResponse.setSpentTime(RequestUtil.getSpentTime());
        return baseResponse;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public long getSpentTime() {
        return spentTime;
    }

    public void setSpentTime(long spentTime) {
        this.spentTime = spentTime;
    }
}
