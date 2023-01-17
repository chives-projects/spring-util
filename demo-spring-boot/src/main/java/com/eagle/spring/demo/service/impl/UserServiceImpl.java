package com.eagle.spring.demo.service.impl;

import com.eagle.spring.demo.mapper.mysql.UserMapper;
import com.eagle.spring.demo.po.User;
import com.eagle.spring.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User selectById(Integer id) {
        return userMapper.selectById(id);
    }

    @Override
    public int insert(String username) {
        return userMapper.insert(username);
    }

    @Override
    public String getMysql() {
        return userMapper.getMysql();
    }
}
