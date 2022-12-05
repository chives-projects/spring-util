package com.csc.spring.context.helper;

import com.csc.spring.context.ioc.IOCContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @description: 异步线程池帮助类, TaskExecutionAutoConfiguration
 * @author: csc
 * @Create: 2022/12/01
 */
public class ThreadPoolHelper {
    /**
     * 获取线程池
     *
     * @return ThreadPoolTaskExecutor
     */
    public static ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        return IOCContext.getBean(ThreadPoolTaskExecutor.class);
    }
}
