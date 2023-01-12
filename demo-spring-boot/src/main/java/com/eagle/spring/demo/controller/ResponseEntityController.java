package com.eagle.spring.demo.controller;

import com.eagle.spring.demo.po.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: csc
 * @create: 2022/12/13
 * @Version: 1.0
 */
@RestController
@RequestMapping("responseEntity")
public class ResponseEntityController {
    @GetMapping("/string")
    ResponseEntity<String> string() {
        return ResponseEntity.ok()
                .header("Custom-Header", "foo")
                .body("Custom header set");
    }

    @GetMapping("/object")
    public ResponseEntity<User> object() {
        return new ResponseEntity<>(new User(), HttpStatus.OK);
    }
}
