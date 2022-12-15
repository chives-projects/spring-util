package com.eagle.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @Description:
 * @Author: csc
 * @create: 2022/12/13
 * @Version: 1.0
 */
public interface InitializingAutoConfig extends InitializingBean, DisposableBean {

    // todo:优化为获取对应pom文件的artifactId
    String MODULE_NAME = "eagle-spring-boot-starter";
    Logger logger = LoggerFactory.getLogger(InitializingAutoConfig.class);

    @Override
    default void destroy() throws Exception {
        logger.info("<== destroy---[{}][{}]", MODULE_NAME, this.getClass().getSimpleName());
    }

    @Override
    default void afterPropertiesSet() throws Exception {
        logger.info("==> initialize---[{}][{}]", MODULE_NAME, this.getClass().getSimpleName());
    }
}
