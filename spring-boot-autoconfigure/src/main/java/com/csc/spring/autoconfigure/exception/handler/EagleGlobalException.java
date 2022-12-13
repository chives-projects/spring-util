package com.csc.spring.autoconfigure.exception.handler;

import com.csc.common.constant.AttributeInfo;
import com.csc.common.constant.HeaderInfo;
import com.csc.common.exception.BasicException;
import com.csc.common.exception.PrintExceptionInfo;
import com.csc.common.po.BaseLogger;
import com.csc.common.utils.UUIDUtils;
import com.csc.common.utils.character.JSONUtils;
import com.csc.common.utils.spring.RequestUtil;
import com.csc.spring.core.context.ContextHolder;
import com.csc.spring.core.helper.RequestHelper;
import com.csc.spring.logback.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.Objects;

/**
 * @Description :  异常处理基础类
 * @Author: csc
 * @create: 2022/12/13
 */
public class EagleGlobalException {

    private static final Logger logger = LoggerFactory.getLogger(DefaultGlobalExceptionHandler.class);

    /**
     * 获取异常堆栈信息并记录到error文件中
     */
    public void recordErrorMsg(Throwable ex, HttpServletRequest request) {
        String errorMsg = PrintExceptionInfo.printErrorInfo(ex);
        if (ex instanceof BasicException) {
            BasicException systemException = (BasicException) ex;
            errorMsg = MessageFormat.format("业务异常，异常码是【{0}】，异常消息是【{1}】，异常详情{2}", systemException.getCode(), systemException.getMessage(), errorMsg);
        }
        logger.error(errorMsg);
        //记录错误日志
        recordErrorLogger(request, ex, errorMsg);
    }

    /**
     * 记录错误日志
     *
     * @param request
     * @param errorMsg
     */
    private void recordErrorLogger(HttpServletRequest request, Throwable ex, String errorMsg) {
        if (Objects.isNull(request)) {
            return;
        }
        if (Objects.nonNull(request.getAttribute(AttributeInfo.STAGE))) {
            return;
        }
        try {
            //事务流水号
            String traceId = request.getHeader(HeaderInfo.TRACE_ID) == null ? UUIDUtils.randomSimpleUUID() : request.getHeader(HeaderInfo.TRACE_ID);

            BaseLogger baseLogger = new BaseLogger();
            //系统编号
            baseLogger.setSystemNumber(ContextHolder.current().getSystemNumber());
            //事务唯一编号
            baseLogger.setTraceId(traceId);
            //请求URL
            baseLogger.setRequestUrl(request.getRequestURI());
            //客户端IP
            baseLogger.setClientIp(RequestUtil.getClientIp());
            //服务端IP
            baseLogger.setServerIp(RequestUtil.getServerIp());
            //请求参数
            baseLogger.setRequestParams(RequestHelper.getApiArgs(request));
            if (ex instanceof BindException) {
                BindingResult bindingResult = ((BindException) ex).getBindingResult();
                //todo:

            }
            //响应体
            baseLogger.setResponseBody(errorMsg);
            //耗时(未处理任何逻辑)
            baseLogger.setSpentTime(0L);
            //记录日志到文件
            logger.info(JSONUtils.toJSONString(baseLogger));
        } catch (Exception exception) {
            logger.error(MessageFormat.format("记录错误日志异常：{0}", PrintExceptionInfo.printErrorInfo(exception)));
        } finally {
            //由于获取参数中会初始化上下文，清除防止OOM
            ContextHolder.unbind(true);
        }
    }
}
