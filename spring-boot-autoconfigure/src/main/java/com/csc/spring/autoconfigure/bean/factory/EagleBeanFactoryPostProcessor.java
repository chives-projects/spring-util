package com.csc.spring.autoconfigure.bean.factory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * @author: csc
 * @description: bean注册成功但未实例化之前调用的后置处理器，用来更改BeanDefinition
 * @create: 2022/12/13
 */
@SuppressWarnings("all")
public class EagleBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
