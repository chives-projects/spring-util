package com.csc.spring.demo.controller;

import com.csc.spring.demo.po.User;
import com.csc.spring.demo.po.UserRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description: 参数控制器
 * @create: 2022/11/18
 */
@RestController
@RequestMapping("param")
public class ParamController {

    @GetMapping("getPath")
    public String getPath(@PathVariable String request) {
        return request;
    }

    @GetMapping("getParam")
    public UserRequest getParam(UserRequest request) {
        return request;
    }

    @PostMapping("post")
    public UserRequest getUser(@Validated @RequestBody UserRequest request) {
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
