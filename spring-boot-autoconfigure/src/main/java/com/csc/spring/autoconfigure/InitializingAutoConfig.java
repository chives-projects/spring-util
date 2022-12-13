package com.csc.spring.autoconfigure;

import com.csc.spring.logback.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @Description:
 * @Author: csc
 * @create: 2022/12/13
 * @Version: 1.0
 */
public interface InitializingAutoConfig extends InitializingBean, DisposableBean {
    String MODULE_NAME = "spring-boot-starter";
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
