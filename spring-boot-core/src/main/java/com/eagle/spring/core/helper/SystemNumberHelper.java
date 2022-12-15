package com.eagle.spring.core.helper;

import com.eagle.spring.core.context.IOCContext;
import com.eagle.spring.core.context.config.ContextProperties;

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
