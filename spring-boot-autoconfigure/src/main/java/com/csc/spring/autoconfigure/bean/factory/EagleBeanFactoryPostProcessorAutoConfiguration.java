package com.csc.spring.autoconfigure.bean.factory;

import com.csc.spring.autoconfigure.InitializingAutoConfig;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @author: csc
 * @description:
 * @create: 2022/12/13
 */
@AutoConfiguration
public class EagleBeanFactoryPostProcessorAutoConfiguration implements InitializingAutoConfig {
    @Bean
    public static EagleBeanFactoryPostProcessor grainBeanFactoryPostProcessor() {
        return new EagleBeanFactoryPostProcessor();
    }
}
