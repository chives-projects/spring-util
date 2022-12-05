package com.csc.spring.context.helper;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.csc.common.constant.HeaderInfo;
import com.csc.common.utils.UUIDUtils;
import com.csc.common.utils.spring.RequestUtil;

import java.util.Objects;

/**
 * @description: 当前上下文请求信息
 * @author: csc
 * @Create: 2022/12/01
 */
public class ContextHolder {

    private static final ThreadLocal<RequestHolder> CONTEXT = new TransmittableThreadLocal<>();

    /**
     * 设置当前线程持有的数据源
     */
    public static void bind(RequestHolder requestHolder) {
        CONTEXT.set(requestHolder);
    }

    /**
     * 获取当前线程持有的数据源
     */
    @Deprecated
    public static RequestHolder get() {
        return current();
    }

    /**
     * 获取当前线程持有的数据源
     */
    public static RequestHolder current() {
        if (CONTEXT.get() == null) {
            bind(new RequestHolder());
        }
        return CONTEXT.get();
    }

    /**
     * 是否移除上下文
     */
    public static void unbind(boolean flag) {
        if (flag) {
            CONTEXT.remove();
        }
    }

    /**
     * 非容器上下文移除，如果为true,则不移除，否则移除
     */
    public static void unbind() {
        if (!CONTEXT.get().isServletContext()) {
            CONTEXT.remove();
        }
    }


    public static class RequestHolder {
        /**
         * 系统编号
         */
        private String systemNumber;
        /**
         * 事务唯一编号
         */
        private String traceId;
        /**
         * 通行证账号
         */
        private String userId;
        /**
         * 登录号
         */
        private String accountCode;
        /**
         * appType
         */
        private String appType;
        /**
         * 版本号
         */
        private String appVersion;
        /**
         * 设备ID
         */
        private String deviceId;
        /**
         * 客户端IP
         */
        private String clientIp;
        /**
         * 服务端IP
         */
        private String serverIp;
        /**
         * 操作人
         */
        private String operator;
        /**
         * 是否servlet容器上下文，默认：false
         */
        private boolean servletContext;

        public RequestHolder() {
            this.systemNumber = SystemNumberHelper.getSystemNumber();
            this.clientIp = RequestUtil.getClientIp();
            this.serverIp = RequestUtil.getServerIp();
            if (RequestUtil.isServletContext()) {
                this.traceId = RequestUtil.getRequest().getHeader(HeaderInfo.TRACE_ID);
                this.servletContext = true;
            }
            if (Objects.isNull(traceId)) {
                this.traceId = UUIDUtils.randomSimpleUUID();
            }
        }

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public String getSystemNumber() {
            return systemNumber;
        }

        public void setSystemNumber(String systemNumber) {
            this.systemNumber = systemNumber;
        }

        public String getTraceId() {
            return traceId;
        }

        public void setTraceId(String traceId) {
            this.traceId = traceId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getAccountCode() {
            return accountCode;
        }

        public void setAccountCode(String accountCode) {
            this.accountCode = accountCode;
        }

        public String getAppType() {
            return appType;
        }

        public void setAppType(String appType) {
            this.appType = appType;
        }

        public String getAppVersion() {
            return appVersion;
        }

        public void setAppVersion(String appVersion) {
            this.appVersion = appVersion;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getClientIp() {
            return clientIp;
        }

        public void setClientIp(String clientIp) {
            this.clientIp = clientIp;
        }

        public String getServerIp() {
            return serverIp;
        }

        public void setServerIp(String serverIp) {
            this.serverIp = serverIp;
        }

        public boolean isServletContext() {
            return servletContext;
        }

        public void setServletContext(boolean servletContext) {
            this.servletContext = servletContext;
        }
    }

    /**
     * API请求阶段
     */
    public enum Stage {
        //RequestMappingHandlerMapping校验转发阶段
        MAPPING,
        //Request请求AOP拦截阶段
        REQUEST,
        //Feign请求阶段
        FEIGN,
        //RestTemplate请求阶段
        HTTP,
        //Mybatis日志记录
        MYBATIS,
        //数据库中间件
        MIDDLE,
        //其它阶段
        OTHER;
    }
}
