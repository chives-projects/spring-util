package com.csc.spring.core.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.csc.common.constant.HeaderInfo;
import com.csc.common.po.BaseLogger;
import com.csc.common.utils.UUIDUtils;
import com.csc.common.utils.spring.RequestUtil;
import com.csc.spring.core.helper.SystemNumberHelper;
import org.apache.commons.lang3.StringUtils;

/**
 * @description: 当前上下文请求信息,全链路
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


    public static class RequestHolder extends BaseLogger {
        /**
         * 是否servlet容器上下文，默认：false
         */
        private boolean servletContext;

        public RequestHolder() {
            setSystemNumber(SystemNumberHelper.getSystemNumber());
            setClientIp(RequestUtil.getClientIp());
            setServerIp(RequestUtil.getServerIp());
            if (RequestUtil.isServletContext()) {
                setTraceId(RequestUtil.getRequest().getHeader(HeaderInfo.TRACE_ID));
                this.servletContext = true;
            }
            if (StringUtils.isEmpty(getTraceId())) {
                setTraceId(UUIDUtils.randomSimpleUUID());
            }
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
