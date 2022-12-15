package com.csc.spring.demo.controller;

import com.eagle.logback.EagleLoggerFactory;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: Description
 * @create: 2022/11/18
 */
@Slf4j
@RestController
@RequestMapping("logback")
public class LogbackController {
    private static final Logger baseLogger = LoggerFactory.getLogger(LogbackController.class);
    private static final Logger groupLoggerFile = EagleLoggerFactory.getGroupLogger(LogbackController.class, "p1/p11", "test");
    private static final Logger groupLogger = EagleLoggerFactory.getGroupLogger(LogbackController.class, "p1/p11");
    private static final Logger moduleLogger = EagleLoggerFactory.getModuleLogger(LogbackController.class, "p2", "p21");

    @GetMapping("debug")
    public String debug() {
        baseLogger.error("root + error------" + System.currentTimeMillis());
        baseLogger.warn("root + warn------" + System.currentTimeMillis());
        baseLogger.info("root + info------" + System.currentTimeMillis());
        baseLogger.debug("root + debug------" + System.currentTimeMillis());
        baseLogger.trace("trace------" + System.currentTimeMillis());
        System.out.println("-------------------");

        groupLoggerFile.error("groupLoggerFile + error------" + System.currentTimeMillis());
        groupLoggerFile.warn("groupLoggerFile + warn------" + System.currentTimeMillis());
        groupLoggerFile.info("groupLoggerFile + info------" + System.currentTimeMillis());
        groupLoggerFile.debug("groupLoggerFile + debug------" + System.currentTimeMillis());
        groupLoggerFile.trace("groupLoggerFile + trace------" + System.currentTimeMillis());
        System.out.println("-------------------");

        groupLogger.error("groupLogger + error------" + System.currentTimeMillis());
        groupLogger.warn("groupLogger + warn------" + System.currentTimeMillis());
        groupLogger.info("groupLogger + info------" + System.currentTimeMillis());
        groupLogger.debug("groupLogger + debug------" + System.currentTimeMillis());
        groupLogger.trace("groupLogger + trace------" + System.currentTimeMillis());
        System.out.println("-------------------");

        //只有对应级别的才能打印在日志中
        moduleLogger.info("ModuleLogger + info-----------------" + System.currentTimeMillis());
        moduleLogger.error("ModuleLogger + error------" + System.currentTimeMillis());
        moduleLogger.debug("ModuleLogger + debug-----------------" + System.currentTimeMillis());

        return "success";
    }
}
