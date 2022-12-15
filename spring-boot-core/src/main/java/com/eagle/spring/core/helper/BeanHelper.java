package com.eagle.spring.core.helper;

import com.eagle.common.enums.ApplicationStatus;
import com.eagle.common.exception.BusinessException;
import com.eagle.common.utils.character.StrUtils;
import com.eagle.spring.core.context.IOCContext;

import java.text.MessageFormat;

/**
 * @description: bean获取帮助类
 * @author: csc
 * @Create: 2022/12/01
 */
public class BeanHelper {
    /**
     * 获取容器中指定接口的实现类，beanName=接口类名首字母小写+suffix
     *
     * @param clazz
     * @param suffix
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz, String suffix) {
        if (!clazz.isInterface()) {
            throw new BusinessException(ApplicationStatus.EXCEPTION.getCode(), "必须为接口类型");
        }
        String beanName = MessageFormat.format("{0}{1}", StrUtils.toLowerFirstCase(clazz.getSimpleName()), suffix);
        if (IOCContext.containsBean(beanName)) {
            return IOCContext.getBean(beanName, clazz);
        }
        throw new BusinessException(ApplicationStatus.EXCEPTION.getCode(), "实例对象不存在");
    }
}
