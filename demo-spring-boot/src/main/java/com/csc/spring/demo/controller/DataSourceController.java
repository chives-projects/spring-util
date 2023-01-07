package com.csc.spring.demo.controller;

import com.csc.spring.demo.mapper.mysql.UserMapper;
import com.csc.spring.demo.po.User;
import com.csc.spring.demo.service.OracleService;
import com.csc.spring.demo.service.UserService;
import com.eagle.spring.datasource.interceptor.TargetDataSource;
import com.eagle.spring.datasource.support.helper.SqlSessionFactoryHelper;
import com.google.common.collect.Lists;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("dataSource")
public class DataSourceController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private OracleService oracleService;

    /**
     * foreach 模式批量插入数据库
     *
     * @param num
     * @return
     */
    @GetMapping("batchSimple/{num}")
    @TargetDataSource("mysql")
    public long batchSimple(@PathVariable Integer num) {
        List<User> list = Lists.newArrayList();
        for (int i = 0; i < num; i++) {
            User user = new User();
            user.setUsername("name" + i);
            list.add(user);
        }
        long start = System.currentTimeMillis();
        userMapper.insertByBatch(list);
        return System.currentTimeMillis() - start;
    }

    /**
     * batch模式批量插入数据库
     *
     * @param num
     * @return
     */
    @GetMapping("batch/{num}")
    @TargetDataSource("mysql")
    @Transactional(rollbackFor = Exception.class)
    public long getBatch(@PathVariable Integer num) {
        long start = System.currentTimeMillis();
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryHelper.getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            for (int i = 0; i < num; i++) {
                userMapper.insert("name" + i);

                // 手动提交，提交后无法回滚
                if (i % 1000 == 0) {
                    sqlSession.commit();
                }
            }
            // 手动提交，提交后无法回滚
            sqlSession.commit();
            // 清理缓存，防止溢出
            sqlSession.clearCache();
        } catch (Exception exception) {
            // 没有提交的数据可以回滚
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
        return System.currentTimeMillis() - start;
    }

    /**
     * 逐条插入数据库
     *
     * @param num
     * @return
     */
    @GetMapping("insertItems/{num}")
    @TargetDataSource("mysql")
    public long insertItem(@PathVariable Integer num) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            userMapper.insert("name" + i);
        }
        return System.currentTimeMillis() - start;
    }

    @GetMapping("getUser/{id}")
    public User getUser(@PathVariable int id) {
        User user = userMapper.selectById(id);
        return user;
    }


    @GetMapping("getMysql")
    public String getMysql() {
        return userService.getMysql();
    }

    @GetMapping("getOracle")
    public String getOracle() {
        return oracleService.getOracle();
    }


    @GetMapping("getMyOracle")
    public String getMyOracle() {
        return oracleService.getMyOracle();
    }
}
