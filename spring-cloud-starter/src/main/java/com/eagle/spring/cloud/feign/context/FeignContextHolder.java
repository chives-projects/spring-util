package com.eagle.spring.cloud.feign.context;


import com.eagle.common.po.BaseLogger;
import org.springframework.core.NamedThreadLocal;

/**
 * @Description: Feign上下文持有对象
 * @Author: csc
 * @create: 2023-01-20
 */
public class FeignContextHolder {
    private static final ThreadLocal<BaseLogger> CONTEXT = new NamedThreadLocal<>("Feign Logger Context");

    /**
     * 设置当前线程持有的数据源
     */
    public static void bind(BaseLogger baseLogger) {
        CONTEXT.set(baseLogger);
    }

    /**
     * 获取当前线程持有的数据源
     */
    public static BaseLogger current() {
        return CONTEXT.get();
    }

    /**
     * 删除当前线程持有的数据源
     */
    public static void unbind() {
        CONTEXT.remove();
    }

}
