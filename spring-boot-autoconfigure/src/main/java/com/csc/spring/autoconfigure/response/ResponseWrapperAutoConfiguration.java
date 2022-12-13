package com.csc.spring.autoconfigure.response;

import com.csc.common.constant.CharacterInfo;
import com.csc.spring.core.context.IOCContext;
import com.csc.spring.logback.LoggerFactory;
import com.csc.spring.autoconfigure.InitializingAutoConfig;
import com.csc.spring.autoconfigure.response.handler.ResponseHttpEntityMethodReturnValueHandler;
import com.csc.spring.autoconfigure.response.handler.ResponseHttpHeadersReturnValueHandler;
import com.csc.spring.autoconfigure.response.handler.ResponseMethodReturnValueHandler;
import com.csc.spring.autoconfigure.web.WebProperties;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.HttpEntityMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.HttpHeadersReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author: csc
 * @Description: 控制器返回值配置处理类
 * @Version: 1.0
 */
@AutoConfiguration(after = WebMvcAutoConfiguration.class)
@EnableConfigurationProperties(ResponseWrapperProperties.class)
@ConditionalOnProperty(prefix = ResponseWrapperProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class ResponseWrapperAutoConfiguration implements InitializingAutoConfig {

    private static final Logger logger = LoggerFactory.getLogger(ResponseWrapperAutoConfiguration.class);

    private static final Set<String> DEFAULT_ROUTES = new HashSet<String>() {{
        add("/swagger-resources/**");
        add("/v2/api-docs");
        add("/swagger-ui.html");
        add("/oauth/token");
        add("/error");
    }};

    @Bean
    public Object initResponseWrapper(RequestMappingHandlerAdapter handlerAdapter, ResponseWrapperProperties properties) {
        completeApiPrefix();
        List<HandlerMethodReturnValueHandler> handlers = handlerAdapter.getReturnValueHandlers().stream().map(valueHandler -> {
            if (valueHandler instanceof RequestResponseBodyMethodProcessor) {
                return new ResponseMethodReturnValueHandler(valueHandler, properties);
            }
            if (valueHandler instanceof HttpEntityMethodProcessor) {
                return new ResponseHttpEntityMethodReturnValueHandler(valueHandler, properties);
            }
            if (valueHandler instanceof HttpHeadersReturnValueHandler) {
                return new ResponseHttpHeadersReturnValueHandler(valueHandler);
            }
            return valueHandler;
        }).collect(Collectors.toList());

        handlerAdapter.setReturnValueHandlers(handlers);
        return "UNSET";
    }

    private void completeApiPrefix() {
        ResponseWrapperProperties properties = IOCContext.getBean(ResponseWrapperProperties.class);
        WebProperties webProperties = IOCContext.getBean(WebProperties.class);
        Set<String> excludes = Sets.newHashSet();
        if (ObjectUtils.isNotEmpty(webProperties) && ObjectUtils.isNotEmpty(webProperties.getPath())
                && StringUtils.isNotEmpty(webProperties.getPath().getPrefix())) {
            String prefix = webProperties.getPath().getPrefix();
            for (String path : properties.getExclude())
                excludes.add(StringUtils.join(CharacterInfo.PATH_SEPARATOR, prefix, path));
        }
        properties.setExclude(Sets.union(excludes, DEFAULT_ROUTES));
    }
}
