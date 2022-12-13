package com.csc.spring.demo.controller;

import com.csc.common.exception.RemoteInvokeException;
import com.csc.spring.demo.po.Job;
import com.csc.spring.demo.po.User;
import com.csc.spring.logback.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

/**
 * @description:
 * @author: csc
 * @create: 2022/12/13
 */
@RestController
@RequestMapping("exception")
@Validated
public class ExceptionController {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionController.class);

    @GetMapping("nullPoint")
    public void test1() {
        String s = null;
        s.length();
    }

    /**
     * 只有类上的@Validated对此校验生效
     */
    @GetMapping("validateStr")
    public String validateStr(@NotBlank(message = "str is null") @RequestParam String str) {
        return str;
    }

    @GetMapping("getParamValidate")
    public Job getParamValidate(@Validated Job job) {
        return job;
    }

    @PostMapping("paramValidate")
    public Job paramValidate(@Validated @RequestBody Job job) {
        return job;
    }

    @PostMapping("nestingParamValidate")
    public User nestingParamValidate(@Validated @RequestBody User user) {
        return user;
    }

    @GetMapping("remoteInvokeException")
    public void remoteInvokeException() {
        throw new RemoteInvokeException();
    }

}
