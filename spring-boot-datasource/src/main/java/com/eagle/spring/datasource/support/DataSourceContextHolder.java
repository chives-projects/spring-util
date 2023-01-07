package com.eagle.spring.datasource.support;

import org.springframework.core.NamedThreadLocal;

/**
 * @Description: 线程持有数据源上线文
 * @Author: csc
 * @create: 2023/01/06
 */
public class DataSourceContextHolder {
    /**
     * 当前线程对应的数据源
     */
    private static final ThreadLocal<String> CONTEXT = new NamedThreadLocal<>("Multi data source switching context");

    /**
     * 设置当前线程持有的数据源
     */
    public static void bind(String dataSource) {
        CONTEXT.set(dataSource);
    }

    /**
     * 获取当前线程持有的数据源
     */
    public static String current() {
        return CONTEXT.get();
    }

    /**
     * 删除当前线程持有的数据源
     */
    public static void unbind() {
        CONTEXT.remove();
    }

}
