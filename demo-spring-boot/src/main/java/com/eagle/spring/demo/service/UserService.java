package com.eagle.spring.demo.service;

import com.eagle.spring.demo.po.User;

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
