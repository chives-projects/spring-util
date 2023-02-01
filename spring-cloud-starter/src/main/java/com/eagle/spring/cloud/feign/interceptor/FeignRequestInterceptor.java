package com.eagle.spring.cloud.feign.interceptor;

import com.eagle.common.constant.CharacterInfo;
import com.eagle.common.constant.HeaderInfo;
import com.eagle.common.po.BaseLogger;
import com.eagle.spring.cloud.feign.context.FeignContextHolder;
import com.eagle.spring.core.context.ContextHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

/**
 * @Author: csc
 * @Description: feign请求日志拦截
 * @create: 2023-01-20
 */
public class FeignRequestInterceptor implements RequestInterceptor, PriorityOrdered {

    @Override
    public void apply(RequestTemplate template) {
        //请求header设置事务ID
        template.header(HeaderInfo.TRACE_ID, ContextHolder.current().getTraceId());
        //封装异步日志信息
        BaseLogger baseLogger = new BaseLogger();
        //事务唯一编号
        baseLogger.setTraceId(ContextHolder.current().getTraceId());
        //时间
//        baseLogger.setTriggerTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DateFormatEnum.YYYY_MM_DD_HH_MM_SS_SSS.getFormat())));
        //请求url
        baseLogger.setRequestUrl(String.format("%s%s", StringUtils.rightPad(template.feignTarget().url(), 1, CharacterInfo.PATH_SEPARATOR), RegExUtils.replaceFirst(template.url(), CharacterInfo.PATH_SEPARATOR, "")));
        //请求参数
//        baseLogger.getRequestParams().put(AttributeInfo.HEADERS, template.headers());
        // 将日志信息放入请求对象
        FeignContextHolder.bind(baseLogger);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
