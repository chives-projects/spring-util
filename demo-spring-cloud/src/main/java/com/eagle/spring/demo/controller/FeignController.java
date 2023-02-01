package com.eagle.spring.demo.controller;

import com.eagle.common.po.BaseResponse;
import com.eagle.common.utils.servlet.RequestUtils;
import com.eagle.spring.demo.handler.DefaultFeignHandler;
import com.eagle.spring.demo.handler.UserFeignHandler;
import com.eagle.spring.demo.po.User;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 自定义Feign控制器
 * @Author: csc
 */
@RestController
@RequestMapping("feign")
public class FeignController {
    @Autowired
    private DefaultFeignHandler defaultFeignHandler;
    @Autowired
    private UserFeignHandler customFeignHandler;

    @GetMapping("getDefault")
    public BaseResponse<String> getDefault(HttpServletRequest request) {
        int timeout = NumberUtils.toInt(request.getParameter("timeout"), 0);
        return defaultFeignHandler.getConnect(timeout);
    }

    @GetMapping("getUser")
    public BaseResponse<User> getUser(HttpServletRequest request) {
        int timeout = NumberUtils.toInt(request.getParameter("timeout"), 0);
        User user = new User();
        user.setUsername("user");
        return customFeignHandler.getUser(user);
    }


    @GetMapping("connect")
    public String connect(int timeout) throws InterruptedException {
        Thread.sleep(timeout);
        return "connect";
    }

    @PostMapping("custom")
    public User custom(@RequestBody User user) throws InterruptedException {
        int timeout = NumberUtils.toInt(RequestUtils.getRequest().getParameter("timeout"), 0);
        Thread.sleep(timeout);
        user.setUsername("Username");
        return user;
    }
}
