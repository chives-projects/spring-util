package com.csc.spring.demo.service;

import com.csc.spring.demo.po.User;

/**
 * @Description:
 * @Author: csc
 * @Create: 2023-01-07
 */
public interface UserService {
    User selectById(Integer id);

    int insert(String username);

    String getMysql();
}
