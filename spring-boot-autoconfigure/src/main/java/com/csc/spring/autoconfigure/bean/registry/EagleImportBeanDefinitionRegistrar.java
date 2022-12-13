package com.csc.spring.autoconfigure.bean.registry;

import com.csc.spring.logback.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author: csc
 * @description:
 * @create: 2022/12/13
 */
public class EagleImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    private static final Logger logger = LoggerFactory.getLogger(EagleImportBeanDefinitionRegistrar.class);

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {
        registerBeanDefinitions(importingClassMetadata, registry);
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        logger.info("--------ImportBeanDefinitionRegistrar-------");
    }
}
