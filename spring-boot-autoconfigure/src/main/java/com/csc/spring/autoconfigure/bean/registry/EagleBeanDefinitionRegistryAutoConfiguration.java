package com.csc.spring.autoconfigure.bean.registry;

import com.csc.spring.autoconfigure.InitializingAutoConfig;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * @author: csc
 * @description:
 * @create: 2022/12/13
 */
@AutoConfiguration
//@Conditional(MacOsCondition.class)
@Import(EagleImportBeanDefinitionRegistrar.class)
public class EagleBeanDefinitionRegistryAutoConfiguration implements InitializingAutoConfig {
    /**
     * spring BeanFacory的后置处理器，会在IOC容器执行扫描注册（@ComponentScan和@ComponentScans）、自动化配置加载注册之前执行，提前将bean注入到IOC容器
     *
     * @return
     */
    @Bean
    public EagleBeanDefinitionRegistryPostProcessor EagleBeanDefinitionRegistryPostProcessor() {
        return new EagleBeanDefinitionRegistryPostProcessor();
    }
}
