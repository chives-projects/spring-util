package com.csc.spring.demo.controller;

import com.csc.spring.demo.po.User;
import com.csc.spring.demo.po.UserRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * @description: 参数控制器
 *
 * @create: 2022/11/18
*/
@RestController
@RequestMapping("api/param")
public class ParamController {

    @PostMapping("get1")
    public UserRequest getUser(@Validated @RequestBody UserRequest request) {
        //throw new IllegalArgumentException("非法参数异常");
        return request;
    }

    @GetMapping("get2")
    public UserRequest getUser2(UserRequest request) {
        return request;
    }


    @PostMapping("postList")
    public List<User> postList(@RequestBody List<User> list) {
        return list;
    }

    @PostMapping("postArray")
    public User[] postList(@RequestBody User[] list) {
        return list;
    }
}
