package com.csc.spring.common.po;

/**
 * @Description: 日志基类
 * @Package: com.csc.spring.common.po.BaseLog
 * @Author: csc
 * @create: 2022/11/24
 * @Version: 1.0
 */
public class BaseLogger {
    /**
     * 系统编号
     */
    private String systemNumber;
    /**
     * 请求唯一编号
     */
    private String tId;
    /**
     * 请求URL
     */
    private String requestUrl;
    /**
     * 请求Method
     */
    private String method;
    /**
     * 请求类型
     */
    private String contentType;
    /**
     * 日志类型
     */
    private String level;
    /**
     * 客户端IP
     */
    private String clientId;
    /**
     * 服务端IP
     */
    private String serverIp;
    /**
     * 请求参数
     */
    private Object requestParams;

    private Object responseBody;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSystemNumber() {
        return systemNumber;
    }

    public void setSystemNumber(String systemNumber) {
        this.systemNumber = systemNumber;
    }

    public String gettId() {
        return tId;
    }

    public void settId(String tId) {
        this.tId = tId;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Object getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(Object requestParams) {
        this.requestParams = requestParams;
    }

    public Object getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(Object responseBody) {
        this.responseBody = responseBody;
    }
}
