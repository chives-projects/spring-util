package com.csc.spring.context.helper;

import com.csc.spring.context.config.context.ContextProperties;
import com.csc.spring.context.ioc.IOCContext;

/**
 * @description: 系统编号帮助类
 * @author: csc
 * @Create: 2022/12/01
 */
public class SystemNumberHelper {
    /**
     * 获取系统编号
     *
     * @return
     */
    public static String getSystemNumber() {
        return IOCContext.getBean(ContextProperties.class).getSystemNumber();
    }
}
