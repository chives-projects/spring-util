package com.eagle.spring.cloud.feign.logger;


import feign.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: csc
 * @Description: 自定义Feign日志
 * @create: 2023-01-20
 */
public class FeignLogger extends Logger {
    public static final org.slf4j.Logger logger = LoggerFactory.getLogger(FeignLogger.class);

    /**
     * 记录Feign调试日志
     *
     * @param configKey FeignClient 类名#方法名
     * @param format    日志格式化字符串 如：%s%s
     * @param args      格式化参数
     */
    @Override
    protected void log(String configKey, String format, Object... args) {
        logger.info(String.format(methodTag(configKey) + format, args));
    }
}
