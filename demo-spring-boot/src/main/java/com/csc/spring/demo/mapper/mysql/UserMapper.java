package com.csc.spring.demo.mapper.mysql;

import com.csc.spring.demo.po.User;
import com.eagle.spring.datasource.interceptor.TargetDataSource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description:
 * @Author: csc
 * @Create: 2023-01-06
 */
@Mapper
public interface UserMapper {
    User selectById(Integer id);

    int insert(String username);

    int insertByBatch(@Param("list") List<User> list);

    /**
     * 查询接口
     */
    @TargetDataSource("mysql")
    String getMysql();
}
